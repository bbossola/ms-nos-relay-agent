package com.workshare.msnos.relay_agent.commands;

import java.util.Set;

import com.workshare.msnos.core.Cloud;
import com.workshare.msnos.core.Gateway;
import com.workshare.msnos.core.protocols.ip.Endpoint;
import com.workshare.msnos.core.protocols.ip.Endpoints;
import com.workshare.msnos.core.protocols.ip.www.WWWGateway;
import com.workshare.msnos.relay_agent.Command;
import com.workshare.msnos.relay_agent.ui.SysConsole;

public class ListEndpointsCommand implements Command {

    private final Cloud cloud;
    
    public ListEndpointsCommand(Cloud ucloud) {
        super();
        this.cloud = ucloud;
    }


    @Override
    public String description() {
        return "List all endpoints";
    }

    @Override
    public void execute() throws Exception {

        final Set<Gateway> gates = cloud.getGateways();
        SysConsole.out.println("Gates: "+gates.size());
        for (Gateway gate : gates) {
            if (gate instanceof WWWGateway)
                continue;
            
            Endpoints endpoints = gate.endpoints();
            SysConsole.out.println("  "+gate.name()+" gate:");
            for (Endpoint endpoint : endpoints.all()) {
                SysConsole.out.println("    "+endpoint);
            }
        }
    }
    
}
