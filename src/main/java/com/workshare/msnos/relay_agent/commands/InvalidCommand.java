package com.workshare.msnos.relay_agent.commands;

import com.workshare.msnos.relay_agent.Command;

public class InvalidCommand implements Command {

	@Override
	public String description() {
		return "Invalid action";
	}

	@Override
	public void execute() throws Exception {
		System.out.printf("Invalid action requested\n");	
	}
}
