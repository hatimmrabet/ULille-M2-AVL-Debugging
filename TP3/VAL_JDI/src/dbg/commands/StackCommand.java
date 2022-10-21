package dbg.commands;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;

import java.util.List;
import java.util.stream.Collectors;

public class StackCommand implements Command {

    @Override
    public void execute(LocatableEvent event) {
        System.out.println("======> CommandStack.execute()");
        for(StackFrame f : getStack(event)) {
            System.out.println(f.location().method().name());
        }
        System.out.println("==============================");
    }

    public List<StackFrame> getStack(LocatableEvent event) {
        try {
            return event.thread().frames();
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
    }
}
