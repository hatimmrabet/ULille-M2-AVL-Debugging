package dbg.commands;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;

public class StepOverCommand implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("CommandStepOver.execute()");
        // delete all existing step requests
        vm.eventRequestManager().deleteEventRequests(vm.eventRequestManager().stepRequests());
        // create a new step request with StepLine and StepOver and enable it
        vm.eventRequestManager()
                .createStepRequest(event.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER)
                .enable();
    }
}
