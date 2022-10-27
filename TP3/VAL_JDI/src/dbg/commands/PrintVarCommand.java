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
            StackFrame sf = FrameCommand.getCurrentFrame(event);
            try {
                List<LocalVariable> vars = sf.visibleVariables().stream().filter(v -> v.name().equals(varName)).toList();
                if(vars.size() > 0) {
                    LocalVariable var = vars.get(0);
                    System.out.println(var.name() + " = " + sf.getValue(var));
                    return;
                }
            } catch (AbsentInformationException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Variable not found: " + varName);
        }
    }
}
