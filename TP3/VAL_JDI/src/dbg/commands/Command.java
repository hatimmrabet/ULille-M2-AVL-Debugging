package dbg.commands;

import com.sun.jdi.event.Event;
import com.sun.jdi.event.LocatableEvent;

public interface Command {
    void execute(LocatableEvent event);
}
