package dbg.commands;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.StepRequest;

public class StepCommand implements Command {
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("CommandStep.execute()");
        StepEvent stepEvent = (StepEvent) event;
        StepRequest stepRequest = (StepRequest) stepEvent.request();
        if(stepRequest.isEnabled()) {
            stepRequest.disable();
        }
        stepRequest.virtualMachine().eventRequestManager()
                .createStepRequest(stepEvent.thread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
                .enable();
    }
}
