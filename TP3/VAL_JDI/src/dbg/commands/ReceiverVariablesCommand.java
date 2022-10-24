package dbg.commands;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

import java.util.HashMap;
import java.util.Map;

public class ReceiverVariablesCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("ReceiverVariablesCommand.execute()");
        Map<String, Value> receiverVariables = getReceiverVariables(event);
        if (receiverVariables.size() == 0) {
            System.out.println("No receiver variables");
        } else {
            getReceiverVariables(event).forEach(
                    (k, v) -> System.out.println("ReceiverVariables: " + k + " => " + v));
        }
    }

    public static HashMap<String, Value> getReceiverVariables(LocatableEvent event) {
        HashMap<String, Value> receiverVariables = new HashMap<>();
        ObjectReference receiver = ReceiverCommand.getReceiver(event);
        if(receiver != null) {
            receiver.referenceType().allFields().forEach(field -> {
                receiverVariables.put(field.name(), receiver.getValue(field));
            });
        }
        return receiverVariables;
    }
}
