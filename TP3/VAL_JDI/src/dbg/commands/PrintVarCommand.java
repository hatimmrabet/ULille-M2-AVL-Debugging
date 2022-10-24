package dbg.commands;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

import java.util.List;

public class PrintVarCommand implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("CommandPrintVar.execute()");
        if(args.length == 0) {
            System.out.println("No variable name specified");
        } else {
            String varName = args[0];
            StackCommand stackCommand = new StackCommand();
            List<StackFrame> sf = stackCommand.getStack(event);
            for(StackFrame f : sf) {
                try {
                    List<LocalVariable> vars = f.visibleVariables().stream().filter(v -> v.name().equals(varName)).toList();
                    if(vars.size() > 0) {
                        LocalVariable var = vars.get(0);
                        System.out.println(var.name() + " = " + f.getValue(var));
                    } else {
                        System.out.println("Variable not found: " + varName);
                    }
                } catch (AbsentInformationException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}