package dbg.commands;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;

import java.util.HashMap;
import java.util.List;

public class ArgumentsCommand implements Command {

    public void execute(LocatableEvent event) {
        System.out.println("CommandArguments.execute()");
        HashMap<LocalVariable, Value> args = getArguments(event);
        for (LocalVariable arg : args.keySet()) {
            System.out.println("Arguments: "+arg.name() + " = " + args.get(arg));
        }
    }

    public HashMap<LocalVariable, Value> getArguments(LocatableEvent event) {
        MethodCommand methodCommand = new MethodCommand();
        Method method = methodCommand.getMethod(event);
        HashMap<LocalVariable, Value> args = new HashMap<LocalVariable, Value>();
        try {
            for(LocalVariable arg : method.arguments()) {
                args.put(arg, event.thread().frame(0).getValue(arg));
            }
        } catch (AbsentInformationException | IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
        return args;
    }
}
