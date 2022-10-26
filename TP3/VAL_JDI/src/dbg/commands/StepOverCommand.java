package dbg.commands;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.StepRequest;

public class StepOverCommand implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("CommandStepOver.execute()");
        // execute current line
        StepEvent stepEvent = (StepEvent) event;
        StepRequest stepRequest = (StepRequest) ((StepEvent) event).request();
        if(stepRequest.isEnabled()) {
            stepRequest.disable();
        }
        stepRequest.virtualMachine().eventRequestManager()
                .createStepRequest(stepEvent.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER)
                .enable();

    }
}
