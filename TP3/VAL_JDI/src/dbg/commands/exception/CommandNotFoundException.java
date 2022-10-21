package dbg.commands.exception;

public class CommandNotFoundException extends RuntimeException {

    public CommandNotFoundException(String name) {
        super("Command not found: " + name);
    }
}
