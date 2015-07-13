package com.workshare.msnos.relay_agent;


public interface Command {
	
	public String description()
	;

	public void execute() throws Exception
	;
}
