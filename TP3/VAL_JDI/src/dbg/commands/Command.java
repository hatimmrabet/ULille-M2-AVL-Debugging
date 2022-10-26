package dbg.commands;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.LocatableEvent;

public interface Command {
    void execute(VirtualMachine vm,LocatableEvent event, String[] args) throws AbsentInformationException;
}
