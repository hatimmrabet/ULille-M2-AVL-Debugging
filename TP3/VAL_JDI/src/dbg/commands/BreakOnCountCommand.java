package dbg.commands;

import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;

public class BreakOnCountCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("BreakOnCountCommand.execute()");
        if (args.length == 3){
            String fileName = args[0];
            int lineNumber = Integer.parseInt(args[1]);
            int count = Integer.parseInt(args[2]);
            for(ReferenceType targetClass : vm.allClasses()){
                if(targetClass.name().equals(fileName)){
                    try {
                        Location location = targetClass.locationsOfLine(lineNumber).get(0);
                        BreakpointRequest breakpointRequest= vm.eventRequestManager().createBreakpointRequest(location);
                        breakpointRequest.addCountFilter(count);
                        breakpointRequest.enable();
                        System.out.println("Breakpoint set at " + fileName + ":" + lineNumber + " activated after " + count + " hits");
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Class not found: " + fileName);
        }
        else {
            System.out.println("Usage: break-on-count <file> <line> <count>");
        }



    }
}
