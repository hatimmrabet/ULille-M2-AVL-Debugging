# TP3i mplementation de debuggers avec Java Debug Interface

### Houamed Tarek M'rabet Hatim

## Conception

Le design pattern utilisé est le pattern command. Il permet de définir une interface pour exécuter une action. Cette interface contient une méthode execute(). 

Nous avons implementé toutes les commandes et pour tester nous avons ajouté une classe AClass qui nous permet de tester les commandes sender/receiver. 
Nous avons aussi ajouté dans la classe JDISimpleDebuggee une methode loop afin de tester les commandes de breakpoints.

## Implémentation

### Commandes

Nous avons implementé les commandes suivantes:

* **StepCommand** : permet de faire un step
    
```java
vm.eventRequestManager()
.createStepRequest(event.thread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
.enable();
```

Pour tester la commande, il suffit de lancer le programme et de faire un step.
On peut voir que le programme s'arrete à la ligne suivante.

 ```shell
 StepEvent@dbg.JDISimpleDebuggee:6 in thread main
 Enter command: step
 CommandStep.execute()
 StepEvent@dbg.JDISimpleDebuggee:7 in thread main
 ```

* **StepOverCommand** : permet de faire un step over
```shell
stepRequest.virtualMachine().eventRequestManager()
  .createStepRequest(stepEvent.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER)
  .enable();
  ```

Pour tester la commande, il suffit de lancer le programme et de faire un step.
On peut voir que le programme s'arrete à la ligne suivante.

 ```shell
 StepEvent@dbg.JDISimpleDebuggee:7 in thread main
 Enter command: step-over
 CommandStepOver.execute()
 StepEvent@dbg.JDISimpleDebuggee:8 in thread main
 ```

Le programme s'arrete à la ligne suivante car il y a un appel de fonction et ne s'arrete pas à l'interieur de la fonction.
    

La différence entre les deux commandes est que la première permet de faire un step dans la méthode courante et la deuxième permet de faire un step dans la méthode appelante.
Au niveau du code, la première commande crée un StepRequest avec l'option STEP_INTO et la deuxième crée un StepRequest avec l'option STEP_OVER.


* **ContinueCommand** : permet d'avancer jusqu'au prochain breakpoint
On supprime tout les stepRequest et on continue l'exécution du programme jusqu'au prochain breakpoint.

Pour tester on a un breakpoint à la ligne 6 et à la lige 9. On lance le programme et on fait un continue. On peut voir que le programme s'arrete à la ligne 9.


  ```shell
 Dans un breakpoint: step
 CommandStep.execute()
 StepEvent@dbg.JDISimpleDebuggee:6 in thread main
 Enter command: continue
 CommandContinue.execute()
 BreakpointEvent@dbg.JDISimpleDebuggee:9 in thread main
  ```

* **frameCommand** : renvoie la frame courante
Pour tester on lance le programme et on fait un frame.

 ```shell
 ======> CommandFrame.execute()
 dbg.JDISimpleDebuggee.main(java.lang.String[]) dbg.JDISimpleDebuggee:6
 ```
 
* **temporariesCommand** : renvoie les variables temporaires sous forme de couple (nom, valeur)

On est à la ligne 7,si on execute la commande nous allons voir toutes les variables temporaires avant la ligne 7.

```shell
StepEvent@dbg.JDISimpleDebuggee:7 in thread main
Enter command: temporaries
======> CommandTemporaries.execute()
args = instance of java.lang.String[0] (id=416)
description = "Simple power printer"
==============================
```

* **StackCommand** : renvoie la pile d'appel de methode

Pour tester, nous avons mis un breakpoint à la ligne 12 puis nous avons step jusqu'a arriver dans la methode power et on fait un stack.


   ```shell
   Enter command: step
   CommandStep.execute()
   StepEvent@dbg.JDISimpleDebuggee:17 in thread main
   Enter command: stack
   ======> CommandStack.execute()
   power
   printPower
   main
   ==============================
   StepEvent@dbg.JDISimpleDebuggee:17 in thread main
  ```
* **receiverCommand** : renvoie le receiver de la methode courante
* **senderCommand** : renvoie le sender de la methode courante
Le test est le meme que pour receiver.


Pour tester le receiver nous avons crée une class AClass qui contient une methode.
Nous avons ajouté cette methode au main de la classe simpledebuggee et nous avons mis un breakpoint à la ligne 9.
Nous avons fait un step jusqu'à arriver dans la methode de la classe AClass et on fait un receiver.


```shell
StepEvent@dbg.JDISimpleDebuggee:9 in thread main
Enter command: step
CommandStep.execute()
StepEvent@dbg.AClass:8 in thread main
Enter command: sender
SenderCommand.execute()
Sender: instance of java.lang.Class(reflected class=dbg.JDISimpleDebuggee, id=47)
```

````shell
Enter command: receiver
ReceiverCommand.execute()
Receiver: instance of dbg.AClass(id=499)
StepEvent@dbg.AClass:8 in thread main
````

* **receivervariableCommand** :renvoie et imprime la liste des variables d’instance du
  receveur courant, sous la forme d’un couple nom → valeur

* **methodCommand** : renvoie

Pour tester nous avons step dans notre main puis fait un method.

```shell
Enter command: method
CommandMethod.execute()
Method: main
```

