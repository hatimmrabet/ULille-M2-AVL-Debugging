# TP3: Implementation de debuggers avec Java Debug Interface

- ***Houamed Tarek <tarek.houamed.etu@univ-lille.fr>***
- ***M'rabet Hatim <hatim.mrabetelkhomssi.etu@univ-lille.fr>***
---
## Conception

Le design pattern utilisé est le pattern command. Il permet de définir une interface pour exécuter une action. Cette interface contient une méthode execute(). 

Nous avons implementé toutes les commandes et pour tester nous avons ajouté une classe AClass qui nous permet de tester les commandes sender/receiver. 
Nous avons aussi ajouté dans la classe JDISimpleDebuggee une methode loop afin de tester les commandes de breakpoints.

---
## Implémentation

### Gestion d'avancement de la VM

Pour eviter que la vm avance apres chaque commande, nous avons rassemebler tous les evenements de type StepEvent et BreakpointEvents, pour que lors de l'insertion d'une commande differente de step, step-obver et continue, on execute la commande en restant sur la meme ligne.

Si la commande est step, step-over ou continue, on execute la commande et la vm avance.

### Commandes

Nous avons implementé les commandes suivantes:

**`StepCommand`** : permet de faire un step, et s'il s’agit d’un appel de méthode, l’exécution entre dans cette dernière. Avant chaque step on supprime les stepRequests et on en crée un nouveau, ensuite, on l'active.

```java
vm.eventRequestManager().deleteEventRequests(vm.eventRequestManager().stepRequests());
vm.eventRequestManager().createStepRequest(event.thread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
.enable();
```

Pour tester la commande, il suffit de lancer le programme et de faire un en arrivant à la ligne 12 où on fait appel à la méthode `printPower`, on remarque qu'apres l'execution de step plusieurs fois, il accéde à la fonction à la ligne 22.

```shell
StepEvent@dbg.JDISimpleDebuggee:12 in thread main
Enter command: step
CommandStep.execute()
StepEvent@dbg.JDISimpleDebuggee:12 in thread main
Enter command: step
CommandStep.execute()
StepEvent@dbg.JDISimpleDebuggee:22 in thread main
Enter command: frame
======> CommandFrame.execute()
dbg.JDISimpleDebuggee.printPower(int, int) dbg.JDISimpleDebuggee:22
```
---

**`StepOverCommand`** : permet de faire un step, et s'il s’agit d’un appel de méthode, l’exécution passe à la ligne suivante. Avant chaque step on supprime les stepRequests et on en crée un nouveau, ensuite, on l'active.

```java
stepRequest.virtualMachine().eventRequestManager().createStepRequest(stepEvent.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER)
.enable();
```

Pour tester la commande, on peut faire la meme manipulation que pour la commande step, mais on remarque que l'on passe à la ligne suivante, et on n'accéde pas à la fonction.

```shell
BreakpointEvent@dbg.JDISimpleDebuggee:12 in thread main
Enter command: step-over
CommandStepOver.execute()
StepEvent@dbg.JDISimpleDebuggee:13 in thread main
```

La différence entre les deux commandes est que la première permet d'entrer dans une fonction si on l'appelle, tandis que la deuxième permet de passer à la ligne suivante, en ignerant les appels des fonctions.

Au niveau du code, la seule différence avec la commande StepCommand est que l'on utilise `STEP_OVER` à la place de `STEP_INTO`, et `STEP_LINE` à la place de `STEP_MIN`.

---

**`ContinueCommand`** : permet d'avancer jusqu'au prochain breakpoint
On supprime tout les stepRequest et on continue l'exécution du programme jusqu'au prochain breakpoint.

Pour tester on a un breakpoint à la ligne 6 et on crée un autre à la lige 12. On fait un continue. On peut voir que le programme s'arrete à la ligne 12.


```shell
BreakpointEvent@dbg.JDISimpleDebuggee:6 in thread main
Enter command: break dbg.JDISimpleDebuggee 12
BreakpointsCommand.execute()
Breakpoint set at dbg.JDISimpleDebuggee:12
-Enter command: continue
CommandContinue.execute()
BreakpointEvent@dbg.JDISimpleDebuggee:12 in thread main
```
---
**`frameCommand`** : recupere la frame courante, et affiche la methode et la ligne courante.

Pour tester on lance le programme et on fait un frame, ca donne les details de la ligne où on est.

 ```shell
 ======> CommandFrame.execute()
 dbg.JDISimpleDebuggee.main(java.lang.String[]) dbg.JDISimpleDebuggee:6
 ```
--- 
**`temporariesCommand`** : renvoie les variables de la frame courante sous forme de couple (nom, valeur).

On est à la ligne 7,si on execute la commande nous allons voir toutes les variables temporaires avant la ligne 7.

```shell
StepEvent@dbg.JDISimpleDebuggee:7 in thread main
Enter command: temporaries
======> CommandTemporaries.execute()
args = instance of java.lang.String[0] (id=416)
description = "Simple power printer"
==============================
```
---
**``StackCommand``** : recupere la liste des frames, et affiche la liste les noms des methodes qui améne l’exécution au point courant.

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
---

**``receiverCommand``** : renvoie l'object qui fait appel à la methode courante, il renvoit null si la methode est static.

**``senderCommand``** : renvoie l'object class où on execute la methode courante.

Pour tester le receiver nous avons crée une class AClass qui contient une methode ``getInfo()``.

Nous avons ajouté cette methode au main de la classe JDISimpledebuggee et nous avons mis un breakpoint à la ligne 9.
Nous avons fait un step jusqu'à arriver dans la methode de la classe AClass et on fait un receiver.

```shell
StepEvent@dbg.JDISimpleDebuggee:9 in thread main
Enter command: step
CommandStep.execute()
StepEvent@dbg.AClass:8 in thread main
Enter command: sender
SenderCommand.execute()
Sender: instance of java.lang.Class(reflected class=dbg.JDISimpleDebuggee, id=47)
Enter command: receiver
ReceiverCommand.execute()
Receiver: instance of dbg.AClass(id=499)
StepEvent@dbg.AClass:8 in thread main
````
---
**``receiverVariableCommand``** :renvoie et imprime la liste des variables d’instance du receveur courant, sous la forme d’un couple nom → valeur

Pour tester, on lance la commande quand on est à la fonction ``getInfo()`` de la classe AClass.

```shell
Enter command: receiver-variables
ReceiverVariablesCommand.execute()
ReceiverVariables: name => "SenderTest"
ReceiverVariables: age => 10
```
---
**``methodCommand``** : renvoie le nom de la methode courante. Pour tester, dans la meme methode ``getInfo()`` on lance la commande, et on obtient le nom de la methode.

```shell
Enter command: method
CommandMethod.execute()
Method: getInfo
```
---
**``argumentsCommand``** : renvoie la liste des arguments de la methode courante, sous la forme d’un couple nom → valeur.

Pour tester, on lance la commande quand on est dans la methode ``printPower`` de la classe JDISimpleDebuggee.

```shell
StepEvent@dbg.JDISimpleDebuggee:22 in thread main
Enter command: arguments
CommandArguments.execute()
Arguments: x = 40
Arguments: power = 2
```
---
**``print-var``** : permet de recuperer la frame courante et d’afficher la valeur d’une variable s'il existe, sinon on affiche un message d’erreur.

```shell
BreakpointEvent@dbg.JDISimpleDebuggee:12 in thread main
Enter command: print-var description
CommandPrintVar.execute()
description = "Simple power printer"
```
---
**```breakpointsCommand```** : permet de recuperer tous les breakpoints et d'afficher ceux qui sont actifs.

```shell
BreakpointEvent@dbg.JDISimpleDebuggee:6 in thread main
Enter command: breakpoints
BreakpointsCommand.execute
Breakpoint at dbg.JDISimpleDebuggee:6
Breakpoint at dbg.JDISimpleDebuggee:12
```
---
**``BreakCommand``** : permet de mettre un breakpoint à la class ***(package.className)*** et la ligne passé en parametre, en cas de problem, un message d'erreur est affiché.

```shell
Enter command: break dbg.JDISimpleDebuggee 12
BreakpointsCommand.execute()
Breakpoint set at dbg.JDISimpleDebuggee:12
```
Pour tester, on fait un continue, et l'execution s'arrete à la ligne 12.

```shell
Enter command: frame
======> CommandFrame.execute()
dbg.JDISimpleDebuggee.main(java.lang.String[]) dbg.JDISimpleDebuggee:6
==============================
Enter command: continue
CommandContinue.execute()
BreakpointEvent@dbg.JDISimpleDebuggee:12 in thread main
```
---
**``BreakOnceCommand``**: c'est comme la commande break, mais le breakpoint est supprimé après l'avoir atteint, our cela on a ajouter ***addCountFilter*** avec une valeur de 1.

Pour le tester, on a utiliser la fonction loop, qui continet une boucle qui fait 3 iterations, et on a remarquer que le programme s'arrete qu'une seule fois.

```shell
BreakpointEvent@dbg.JDISimpleDebuggee:12 in thread main
Enter command: break-once dbg.JDISimpleDebuggee 29
BreakOnceCommand.execute()
Breakpoint set at dbg.JDISimpleDebuggee:29
-Enter command: step-over
CommandStepOver.execute()
StepEvent@dbg.JDISimpleDebuggee:13 in thread main
Enter command: step
CommandStep.execute()
StepEvent@dbg.JDISimpleDebuggee:27 in thread main
Enter command: continue
CommandContinue.execute()
BreakpointEvent@dbg.JDISimpleDebuggee:29 in thread main
Enter command: continue
CommandContinue.execute()
VMDeathEvent
...
===End of program.
```
---
**``BreakOnCountCommand``** : permet d'activer un breakpoint qu’après avoir été atteint un certain nombre de fois (le nombre passé en paramètre).

Pour tester, on a utilisé la fonction loop, et on a remarqué que le programme s'arrete 1 fois, apres 2 passage si on lui donne 3 comme parametre. on verifie, en affichant la valeur de i, et on remarque que la valeur de i est 2, donc il s'est activé que lors du 3eme passage.

```shell
-Enter command: break-on-count dbg.JDISimpleDebuggee 29 3
BreakOnCountCommand.execute()
Breakpoint set at dbg.JDISimpleDebuggee:29
-Enter command: continue
CommandContinue.execute()
BreakpointEvent@dbg.JDISimpleDebuggee:12 in thread main
Enter command: continue
CommandContinue.execute()
BreakpointEvent@dbg.JDISimpleDebuggee:29 in thread main
Enter command: print-var i
CommandPrintVar.execute()
i = 2
```
---
**``BreakBeforeMethodCallCommand``** : permet de recuperer la location de la methode passé en parametre, et de mettre un breakpoint à cette location.

Pour tester, on a passé la methode ``printPower`` en parametre, et on a remarqué que le programme s'arrete à la ligne 22 de la methode ``printPower``.

```shell
BreakpointEvent@dbg.JDISimpleDebuggee:6 in thread main
Enter command: break-before-method-call printPower
Breakpoint set at dbg.JDISimpleDebuggee:22
-Enter command: continue
CommandContinue.execute()
BreakpointEvent@dbg.JDISimpleDebuggee:22 in thread main
Enter command: frame
======> CommandFrame.execute()
dbg.JDISimpleDebuggee.printPower(int, int) dbg.JDISimpleDebuggee:22
```