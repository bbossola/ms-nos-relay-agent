package com.workshare.msnos.relay_agent.commands;

import com.workshare.msnos.relay_agent.Command;
import com.workshare.msnos.relay_agent.ui.SysConsole;

public class ExitCommand implements Command {

    private Command[] commands;

    public ExitCommand(Command... commands) {
        this.commands = commands;
    }

    @Override
	public String description() {
		return "Exit";
	}

	@Override
	public void execute() throws Exception {
	    try {
	        SysConsole.out.println("Running exit commands");
	        for (Command command: commands) {
	            SysConsole.out.println("- "+command.description());
	            command.execute();
	        }
	    } finally {
	        SysConsole.out.println("\nExiting, thanks for all the fish :)");
    	    System.exit(0);
	    }
	}

}
