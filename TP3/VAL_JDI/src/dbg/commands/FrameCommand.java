package dbg.commands;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

public class FrameCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("======> CommandFrame.execute()");
        StackFrame sf = getCurrentFrame(event);
        System.out.println(sf.location().method() + " " + sf.location());
        System.out.println("==============================");
    }

    public static StackFrame getCurrentFrame(LocatableEvent event) {
        try {
            return event.thread().frame(0);
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
    }

}
