package com.workshare.msnos.relay_agent;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.workshare.msnos.core.Cloud;
import com.workshare.msnos.core.Cloud.Listener;
import com.workshare.msnos.core.LocalAgent;
import com.workshare.msnos.core.Message;
import com.workshare.msnos.core.Message.Type;
import com.workshare.msnos.core.MsnosException;
import com.workshare.msnos.core.security.KeysStore;
import com.workshare.msnos.core.security.Signer;
import com.workshare.msnos.relay_agent.commands.ExitCommand;
import com.workshare.msnos.relay_agent.commands.JoinCommand;
import com.workshare.msnos.relay_agent.commands.LeaveCommand;
import com.workshare.msnos.relay_agent.commands.ListEndpointsCommand;
import com.workshare.msnos.relay_agent.commands.LogControl;
import com.workshare.msnos.relay_agent.commands.PingAllCommand;
import com.workshare.msnos.relay_agent.commands.RingsCommand;
import com.workshare.msnos.relay_agent.commands.SendToCloudCommand;
import com.workshare.msnos.relay_agent.commands.StatusCommand;
import com.workshare.msnos.relay_agent.commands.SubmenuCommand;
import com.workshare.msnos.relay_agent.commands.UpdateCommand;
import com.workshare.msnos.relay_agent.http.MiniHttpServer;
import com.workshare.msnos.relay_agent.ui.Menu;
import com.workshare.msnos.relay_agent.ui.SysConsole;

public class Server {

    private static final Logger logger = Logger.getLogger("com.workshare");

    private final Cloud cloud;
    private final LocalAgent agent;
    private final MiniHttpServer http;
    private final Command[] commands;
    private final Menu menu;

    public Server(int port) throws IOException {
        cloud = createMicrocloud();
        agent = new LocalAgent(UUID.randomUUID());
        http = new MiniHttpServer(cloud, port);
        commands = createCommands(cloud, agent, http);
        menu = new Menu(commands);
    }
    
    public Server init() {
        http.start();
        return this;
    }

    public void runHeadless() throws Exception {
        SysConsole.out.println("Running join command...");
        new JoinCommand(cloud, agent, http.apis()).execute();
        SysConsole.out.println("Done - Running in headless mode\n");
        dumpStatus();
        
        cloud.addListener(new Listener() {
            @Override
            public void onMessage(Message message) {
                if (message.getType() == Type.PRS || message.getType() == Type.FLT || message.getType() == Type.QNE)
                    dumpStatus();
            }
        });
        
        waitPolitelyForever();
    }

    private synchronized void dumpStatus() {
        try {
            SysConsole.out.println("\n------------------------------------------------------------------------------------------------------------------------------");
            new StatusCommand(SysConsole.get(), cloud, agent, true).execute();
        } catch (Exception ignore) {
        }
    }
    
    private void waitPolitelyForever() throws IOException {
        System.in.read();
    }

    public void runWithConsole() {
        
        while (true) {
            menu.show();
            try {
                menu.selection().execute();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "An error occured!", ex);
            }

            sleep(500L);
        }
    }
    
    private void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }
    
    private Command[] createCommands(Cloud cloud, LocalAgent agent, MiniHttpServer http) {
        Command[] advanced = {
            new SendToCloudCommand(cloud, agent, Message.Type.DSC),
            new SendToCloudCommand(cloud, agent, Message.Type.ENQ),
            new LogControl("protocol"),
            new LogControl("routing"),
        };
        
        Command[] commands = {
            new StatusCommand(SysConsole.get(), cloud, agent, true),
            new JoinCommand(cloud, agent, http.apis()),
            new LeaveCommand(agent),
            new RingsCommand(SysConsole.get(), cloud),
            new UpdateCommand(cloud),
            new ListEndpointsCommand(cloud),
            new PingAllCommand(cloud, agent),
            new SubmenuCommand("advanced...", advanced),
            
            new ExitCommand(new LeaveCommand(agent)),
        };
        
        return commands;
    }
    
    private Cloud createMicrocloud() throws MsnosException {
        Cloud cloud;
        
        String signid = getSecurityId();
        if (signid != null) {
            SysConsole.out.println("ATTENTION! Using secured cloud by id '"+signid+"'");
            cloud = new Cloud(new UUID(111, 222), signid);
        }
        else {
            SysConsole.out.println("Using open cloud :)");
            cloud = new Cloud(new UUID(111, 222));
        }

        return cloud;
    }

    private String getSecurityId() {
        String res = null;
        KeysStore keystore = Signer.DEFAULT_KEYSSTORE;
        if (!keystore.isEmpty()) {
            if (keystore.get("test") != null)
                res = "test";
        }

        return res;
    }

}
