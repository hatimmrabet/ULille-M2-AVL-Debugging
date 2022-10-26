package dbg.commands;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;

import java.util.List;

public class SenderCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("SenderCommand.execute()");
        ObjectReference sender = getSender(event);
        System.out.println("Sender: " + sender);
    }

    public static ObjectReference getSender(LocatableEvent event) {
        List<StackFrame> stacks = StackCommand.getStack(event);
        if (stacks.size() > 1) {
            StackFrame caller = stacks.get(1);
            return caller.location().declaringType().classObject();
        } else {
            return stacks.get(0).location().declaringType().classObject();
        }
    }

}
