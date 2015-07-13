package com.workshare.msnos.relay_agent.commands;

import com.workshare.msnos.core.Cloud;
import com.workshare.msnos.core.LocalAgent;
import com.workshare.msnos.core.Message;
import com.workshare.msnos.core.Message.Type;
import com.workshare.msnos.core.MessageBuilder;
import com.workshare.msnos.relay_agent.Command;
import com.workshare.msnos.relay_agent.ui.SysConsole;

public class SendToCloudCommand implements Command {

    private final Cloud cloud;
    private final LocalAgent agent;
    private final Type type;
    
    public SendToCloudCommand(Cloud cloud, LocalAgent agent, Type type) {
        super();
        this.cloud = cloud;
        this.agent = agent;
        this.type = type;
    }

    @Override
    public String description() {
        return "Sends a "+type+" to the cloud";
    }

    @Override
    public void execute() throws Exception {
        Message message;
        if (agent.getCloud() == null) {
            message = new MessageBuilder(type, cloud, cloud).make();
            SysConsole.out.println("Sending message from the cloud");
        }
        else {
            message = new MessageBuilder(type, agent, cloud).make();
            SysConsole.out.println("Sending message from the agent");
        }
        cloud.send(message);
    }

}
