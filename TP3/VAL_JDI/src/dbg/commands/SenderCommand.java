package dbg.commands;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;

import java.util.List;

public class SenderCommand implements Command {

    @Override
    public void execute(LocatableEvent event) {
        System.out.println("SenderCommand.execute()");
        ObjectReference sender = getSender(event);
        System.out.println("Sender: " + sender);
    }

    public ObjectReference getSender(LocatableEvent event) {
        StackCommand stackCommand = new StackCommand();
        List<StackFrame> stackFrame = stackCommand.getStack(event);
        if(stackFrame.size() > 1) {
            return stackFrame.get(1).thisObject();
        }
        return null;
    }

}
