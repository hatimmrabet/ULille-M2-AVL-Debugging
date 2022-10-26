package dbg.commands;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.EventRequest;

public class ContinueCommand implements Command {
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("CommandContinue.execute()");
        vm.eventRequestManager().deleteEventRequests(vm.eventRequestManager().stepRequests());
    }
}
