package dbg.commands;

import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;

import java.lang.reflect.Method;

public class BreakOnceCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("BreakOnceCommand.execute()");
        if (args.length==2){
            String fileName = args[0];
            int lineNumber = Integer.parseInt(args[1]);
            int count = 1;
            for(ReferenceType targetClass : vm.allClasses()){
                if(targetClass.name().equals(fileName)){
                    try {
                        Location location = targetClass.locationsOfLine(lineNumber).get(0);
                        BreakpointRequest breakpointRequest= vm.eventRequestManager().createBreakpointRequest(location);
                        breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
                        breakpointRequest.addCountFilter(count);
                        breakpointRequest.enable();
                        System.out.println("Breakpoint set at " + fileName + ":" + lineNumber);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else {
            System.out.println("Usage: breakonce <file> <line>");
        }



    }
}
