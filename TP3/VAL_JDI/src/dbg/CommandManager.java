package dbg;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import dbg.commands.*;
import dbg.commands.exception.CommandNotFoundException;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {

    private final HashMap<String, Command> commands = new HashMap<String, Command>();

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public void execute(String cmd, VirtualMachine vm, LocatableEvent event) throws AbsentInformationException {
        String[] args = cmd.split(" ");
        String commandName = args[0];

        Command command = commands.get(commandName);
        if(command == null) {
            System.out.println("Command not found: " + commandName);
        } else {
            command.execute(vm, event, Arrays.copyOfRange(args,1, args.length));
        }
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
        register("print-var", new PrintVarCommand());
        register("break", new BreakCommand());
        register("break-before-method-call", new BreakBeforeMethodCallCommand());

        register("break-once", new BreakOnceCommand());
        register("break-on-count", new BreakOnCountCommand());    }

}
