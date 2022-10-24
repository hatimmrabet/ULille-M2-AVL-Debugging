package dbg.commands;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;

public class TemporariesCommand implements Command {
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("======> CommandTemporaries.execute()");
        FrameCommand frameCommand = new FrameCommand();
        try {
            StackFrame sf = frameCommand.getCurrentFrame(event);
            for(LocalVariable lv : sf.visibleVariables()) {
                if(lv.isArgument())
                {
                    System.out.println(lv.name() + " = " + sf.getValue(lv));
                }
            }
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }

        System.out.println("==============================");
    }
}
