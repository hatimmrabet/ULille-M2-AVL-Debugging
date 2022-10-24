package dbg.commands;

import com.sun.jdi.Method;
import com.sun.jdi.TypeComponent;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

public class MethodCommand implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event, String[] args) {
        System.out.println("CommandMethod.execute()");
        Method method = getMethod(event);
        System.out.println("Method: " + method.name());
    }
    
    public Method getMethod(LocatableEvent event) {
        return event.location().method();
    }

}
