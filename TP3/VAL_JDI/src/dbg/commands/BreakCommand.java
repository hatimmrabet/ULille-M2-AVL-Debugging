package dbg.commands;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;

public class BreakCommand implements Command{

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) throws AbsentInformationException {
        System.out.println("BreakpointsCommand.execute()");
        if(args.length == 2)
        {
            String className = args[0];
            int lineNumber = Integer.parseInt(args[1]);
            for(ReferenceType targetClass : vm.allClasses())
            {
                if(targetClass.name().equals(className))
                {
                    Location location = targetClass.locationsOfLine(lineNumber).get(0);
                    BreakpointRequest bqReq = vm.eventRequestManager().createBreakpointRequest(location);
                    bqReq.enable();
                    System.out.println("Breakpoint set at " + className + ":" + lineNumber);
                    return;
                }
            }
            System.out.println("Class not found: " + className);
        }
        else
        {
            System.out.println("Break take 2 params, usage: break <package>.<class> <line>");
        }
    }
}
