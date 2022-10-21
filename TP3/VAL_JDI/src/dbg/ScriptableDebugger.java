package dbg;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.StepRequest;
import dbg.commands.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

public class ScriptableDebugger {

    private Class debugClass;
    private VirtualMachine vm;

    public VirtualMachine connectAndLaunchVM() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue(debugClass.getName());
        VirtualMachine vm = launchingConnector.launch(arguments);
        return vm;
    }
    public void attachTo(Class debuggeeClass) {

        this.debugClass = debuggeeClass;
        try {
            vm = connectAndLaunchVM();
            enableClassPrepareRequest(vm);
            startDebugger();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalConnectorArgumentsException e) {
            e.printStackTrace();
        } catch (VMStartException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected: " + e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startDebugger() throws VMDisconnectedException, InterruptedException, IOException, AbsentInformationException {
        EventSet eventSet = null;

        CommandManager cmdManager = new CommandManager();
        cmdManager.registerCommands();

        while ((eventSet = vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                System.out.println(event.toString());
                if(event instanceof VMDisconnectEvent)
                {
                    System.out.println("Debuggee output===");
                    InputStreamReader reader = new InputStreamReader(vm.process().getInputStream()) ;
                    OutputStreamWriter writer = new OutputStreamWriter(System.out) ;
                    char[] buf = new char[vm.process().getInputStream().available()];
                    reader.read(buf);
                    writer.write(buf);
                    writer.flush();
                    System.out.println("===End of program.");
                    return;
                }
                if(event instanceof ClassPrepareEvent)
                {
                    setBreakPoint(debugClass.getName(), 6);
                    setBreakPoint(debugClass.getName(), 14);
                }
                if(event instanceof StepEvent)
                {
                    String input = readInput("Enter command: ");
                    cmdManager.execute(input, (LocatableEvent) event);
                }
                if(event instanceof BreakpointEvent)
                {
                    if(readInput("Active Step: ").equals("step"))
                    {
                        enableStepRequest((BreakpointEvent) event);
                    }
                }
                vm.resume();
            }
        }
    }

    private void enableStepRequest(LocatableEvent event) {
        StepRequest stepRequest = vm.eventRequestManager()
                .createStepRequest(event.thread(), StepRequest.STEP_MIN, StepRequest.STEP_OVER);
        stepRequest.enable();
    }

    private void setBreakPoint(String className, int lineNumber) throws AbsentInformationException {
        for(ReferenceType targetClass : vm.allClasses())
        {
            if(targetClass.name().equals(className))
            {
                Location location = targetClass.locationsOfLine(lineNumber).get(0);
                BreakpointRequest bqReq = vm.eventRequestManager().createBreakpointRequest(location);
                bqReq.enable();
            }
        }
    }

    public void enableClassPrepareRequest( VirtualMachine vm) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest() ;
        classPrepareRequest.addClassFilter(debugClass.getName());
        classPrepareRequest.enable() ;
    }

    public String readInput(String msg) throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in);
        char[] buf = new char[1024];
        System.out.print(msg);
        reader.read(buf);
        return new String(buf).trim();
    }


}
