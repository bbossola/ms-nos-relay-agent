package com.workshare.msnos.relay_agent.commands;

import com.workshare.msnos.core.LocalAgent;
import com.workshare.msnos.relay_agent.Command;

public class LeaveCommand implements Command {

    private final LocalAgent agent;
    
    public LeaveCommand(LocalAgent agent) {
        super();
        this.agent = agent;
    }

    @Override
    public String description() {
        return "Leave the cloud";
    }

    @Override
    public void execute() throws Exception {
        if (agent.getCloud() != null)
            agent.leave();
    }

}
