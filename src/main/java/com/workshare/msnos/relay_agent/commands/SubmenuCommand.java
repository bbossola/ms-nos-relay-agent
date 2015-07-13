package com.workshare.msnos.relay_agent.commands;

import com.workshare.msnos.relay_agent.Command;
import com.workshare.msnos.relay_agent.ui.Menu;

public class SubmenuCommand implements Command {

    private final Menu menu;
    private final String name;

    public SubmenuCommand(String name, Command... commands) {
        this.name = name;
        this.menu = new Menu(addExit(commands));
    }

    private Command[] addExit(Command[] commands) {
        Command[] newCommands = new Command[commands.length+1];
        System.arraycopy(commands, 0, newCommands, 1, commands.length);
        newCommands[0] = createBackCommand();
        return newCommands;
    }

    private Command createBackCommand() {
        return new Command(){
            @Override
            public String description() {
                return "<- Back";
            }

            @Override
            public void execute() throws Exception {
            }} ;
    }

    @Override
	public String description() {
		return name;
	}

	@Override
	public void execute() throws Exception {
	    menu.show();
	    menu.selection().execute();
	}

}
