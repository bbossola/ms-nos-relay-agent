package com.workshare.msnos.relay_agent;


public class Main {

    public static void main(String[] args) throws Exception {

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception any) {
            System.err.println("You should specify a TCP port to be used for the agent");
            System.err.println("Optionally you can add INTERACTIVE to allow the user console to show: in that case he agent will not automatically join the cloud");
            return;
        }
        
        Server server = new Server(port).init();
        
        if (args.length > 2 && args[2].equalsIgnoreCase("INTERACTIVE"))
            server.runWithConsole();
        else
            server.runHeadless();
    }
}
