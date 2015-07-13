package com.workshare.msnos.relay_agent.commands;

import java.util.concurrent.TimeUnit;

import com.workshare.msnos.core.Cloud;
import com.workshare.msnos.core.Message;
import com.workshare.msnos.core.MessageBuilder;
import com.workshare.msnos.core.MsnosException;
import com.workshare.msnos.core.Receipt;
import com.workshare.msnos.relay_agent.Command;

public class UpdateCommand implements Command {

    private final Cloud cloud;
    
    public UpdateCommand(Cloud cloud) {
        super();
        this.cloud = cloud;
    }

    @Override
    public String description() {
        return "Updates the information of this cloud";
    }

    @Override
    public void execute() throws Exception {
        update(5, TimeUnit.SECONDS);
    }

    public void update(final long amount, final TimeUnit unit) throws MsnosException {
        
        final Message[] messages = new Message[] {
            new MessageBuilder(Message.Type.DSC, cloud, cloud).make(),
            new MessageBuilder(Message.Type.ENQ, cloud, cloud).make(),
        };
        
        long allotted = amount/messages.length;
        for (Message message : messages) {
            Receipt receipt = cloud.sendSync(message);
            try {
                receipt.waitForDelivery(allotted, unit);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
    }
}
