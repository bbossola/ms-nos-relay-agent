package com.workshare.msnos.relay_agent.commands;

import com.workshare.msnos.core.Cloud;
import com.workshare.msnos.core.LocalAgent;
import com.workshare.msnos.relay_agent.Command;
import com.workshare.msnos.usvc.api.RestApi;

public class JoinCommand implements Command {

    private final Cloud cloud;
    private final LocalAgent agent;
    private final RestApi[] apis;
    
    public JoinCommand(Cloud cloud, LocalAgent agent, RestApi[] apis) {
        super();
        this.cloud = cloud;
        this.agent = agent;
        this.apis = apis;
    }

    @Override
    public String description() {
        return "Join the cloud";
    }

    @Override
    public void execute() throws Exception {
        agent.join(cloud);
        sleep(500);
//        usvc.publish(apis);   // FIXME need the new API support service branch, wait for the merge
    }

    private void sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
