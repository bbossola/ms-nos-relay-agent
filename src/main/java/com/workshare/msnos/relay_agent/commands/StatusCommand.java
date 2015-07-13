package com.workshare.msnos.relay_agent.commands;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.workshare.msnos.core.Agent;
import com.workshare.msnos.core.Cloud;
import com.workshare.msnos.core.LocalAgent;
import com.workshare.msnos.core.RemoteAgent;
import com.workshare.msnos.core.geo.LocationFactory;
import com.workshare.msnos.core.protocols.ip.AddressResolver;
import com.workshare.msnos.core.protocols.ip.Endpoint;
import com.workshare.msnos.core.protocols.ip.Network;
import com.workshare.msnos.relay_agent.Command;
import com.workshare.msnos.relay_agent.ui.Console;

public class StatusCommand implements Command {

    private final static AddressResolver ADDRESS_RESOLVER = new AddressResolver();

    private final Cloud cloud;
    private final LocalAgent agent;
    private final boolean detailed;
    private final Console console;

    public StatusCommand(Console console, Cloud cloud, LocalAgent agent, boolean detailed) {
        super();
        this.console = console;
        this.agent = agent;
        this.cloud = cloud;
        this.detailed = detailed;
    }

    @Override
    public String description() {
        return "Cloud status"+(detailed ? " (detailed) " : "");
    }

    @Override
    public void execute() throws Exception {

        if (detailed) 
            showIPInformation();
        
        console.out().println();
        console.out().print("= Local microservice");
        console.out().print(" - Joined: " + ((agent.getCloud() == null) ? "NO": "Yes"));
        console.out().println();
        dump("", agent);

        final Collection<RemoteAgent> remoteAgents = cloud.getRemoteAgents();
        console.out().println("= Remote Agents: " + remoteAgents.size());
        for (Agent agent : remoteAgents) {
            dump("  ", agent);
        }
    }

    private void showIPInformation() {
        console.out().println("= IP status: ");
        final Network publicIP = ADDRESS_RESOLVER.findPublicIP();
        final Network externalIP = ADDRESS_RESOLVER.findRouterIP();
        console.out().println("- public:   "+publicIP+" (location: "+findLocation(publicIP)+")");
        console.out().println("- external: "+externalIP+" (location: "+findLocation(externalIP)+")");
    }

    private String findLocation(Network net) {
        final String hostString = (net == null ? null : net.getHostString());
        return LocationFactory.DEFAULT.make(hostString).toString();
    }

    private void dump(final String prefix, final Agent agent) {

        console.out().println(prefix + "Agent: " + agent.getIden().getUUID());
        console.out().println(prefix + "  Last seen: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(agent.getAccessTime())));

        final Set<Endpoint> points = agent.getEndpoints();
        console.out().println(prefix + "  Endpoints: " + points.size());
        for (Endpoint ep : points) {
            console.out().println(prefix + "    " + ep);
        }

        console.out().println();
    }
}
