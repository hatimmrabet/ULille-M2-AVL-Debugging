package dbg.commands;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

public class ReceiverCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("ReceiverCommand.execute()");
        ObjectReference thisObject = getReceiver(event);
        System.out.println("Receiver: " + thisObject);
    }

    public ObjectReference getReceiver(LocatableEvent event) {
        try {
            return event.thread().frame(0).thisObject();
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
    }

}
