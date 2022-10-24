package dbg.commands;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;

public class BreakpointsCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("BreakpointsCommand.execute");
        for(BreakpointRequest breakpointRequest : event.virtualMachine().eventRequestManager().breakpointRequests())
        {
            System.out.println("Breakpoint at " + breakpointRequest.location().declaringType().name() + ":" + breakpointRequest.location().lineNumber());
        }
    }
}
