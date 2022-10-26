package dbg.commands;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;

public class BreakOnCountCommand implements Command {

    @Override
    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) throws AbsentInformationException {
        System.out.println("BreakOnCountCommand.execute()");
        if (args.length == 3){
            String fileName = args[0];
            int lineNumber = Integer.parseInt(args[1]);
            int count = Integer.parseInt(args[2]);
            BreakCommand.setBreakpoint(vm, fileName, lineNumber, count);
        }
        else {
            System.out.println("Usage: break-on-count <file> <line> <count>");
        }



    }
}
