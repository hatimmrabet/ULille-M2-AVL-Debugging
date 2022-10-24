package dbg.commands;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

public class FrameCommand implements Command {

    private StackFrame frame;

    public void setFrame(StackFrame frame) {
        this.frame = frame;
    }

    public StackFrame getFrame() {
        return frame;
    }

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("======> CommandFrame.execute()");
        StackFrame sf = getCurrentFrame(event);
        this.setFrame(sf);
        System.out.println(sf.location().method() + " " + sf.location());
        System.out.println("==============================");
    }

    public StackFrame getCurrentFrame(LocatableEvent event) {
        try {
            return event.thread().frame(0);
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
    }

}
