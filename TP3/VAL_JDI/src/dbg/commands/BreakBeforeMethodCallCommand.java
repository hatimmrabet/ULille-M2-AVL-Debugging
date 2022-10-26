package dbg.commands;

import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;

import java.util.List;

public class BreakBeforeMethodCallCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: break-before-method-call <method-name>");
            return;
        }
        String methodName = args[0];
        for(ReferenceType c : vm.allClasses()) {
            List<Method> methods = c.methodsByName(methodName);
            if(methods.size() > 0) {
                for(Method m : c.methodsByName(methodName)) {
                    BreakpointRequest request = vm.eventRequestManager().createBreakpointRequest(m.location());
                    request.enable();
                    System.out.println("Breakpoint set at " + m.location());
                }
                return;
            }
        }
        System.out.println("Method not found: " + methodName);
    }
}
