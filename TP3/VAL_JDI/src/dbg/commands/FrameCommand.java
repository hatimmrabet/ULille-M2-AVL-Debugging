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
        try {
            StackFrame sf = getCurrentFrame(event);
            this.setFrame(sf);
            for(int i = 0; i < sf.visibleVariables().size(); i++) {
                System.out.println(sf.visibleVariables().get(i).name() + " = " + sf.getValue(sf.visibleVariables().get(i)));
            }
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }
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
