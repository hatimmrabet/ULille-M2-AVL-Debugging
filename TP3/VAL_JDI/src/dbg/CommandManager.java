package dbg;

import com.sun.jdi.event.*;
import dbg.commands.*;
import dbg.commands.exception.CommandNotFoundException;

import java.util.HashMap;

public class CommandManager {

    private final HashMap<String, Command> commands = new HashMap<String, Command>();

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public void execute(String name, LocatableEvent event) {
        Command command = commands.get(name);
        if(command == null) {
            throw new CommandNotFoundException(name);
        }
        command.execute(event);
    }

    public void registerCommands() {
        register("step", new StepCommand());
        register("step-over", new StepOverCommand());
        register("continue", new ContinueCommand());
        register("frame", new FrameCommand());
        register("temporaries", new TemporariesCommand());
        register("stack", new StackCommand());
        register("receiver", new ReceiverCommand());
        register("sender", new SenderCommand());
        register("method", new MethodCommand());
        register("arguments", new ArgumentsCommand());
        register("breakpoints", new BreakpointsCommand());
    }

}
