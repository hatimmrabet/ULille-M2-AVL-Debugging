package dbg.commands;

import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.EventRequest;

public class ContinueCommand implements Command {
    public void execute(LocatableEvent event) {
        System.out.println("CommandContinue.execute()");
        EventRequest eventRequest = event.request();
        // desactive les stepping
        eventRequest.disable();
    }
}
