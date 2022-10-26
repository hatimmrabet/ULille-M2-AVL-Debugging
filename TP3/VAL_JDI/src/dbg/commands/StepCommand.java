package dbg.commands;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;

public class StepCommand implements Command {
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("CommandStep.execute()");
        // delete all existing step requests
        vm.eventRequestManager().deleteEventRequests(vm.eventRequestManager().stepRequests());
        // create a new step request with StepMin and StepInto and enable it
        vm.eventRequestManager()
                .createStepRequest(event.thread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
                .enable();
    }
}
