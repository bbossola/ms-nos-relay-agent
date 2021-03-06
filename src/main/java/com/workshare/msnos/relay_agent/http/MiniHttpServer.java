package com.workshare.msnos.relay_agent.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;
import com.workshare.msnos.core.Cloud;
import com.workshare.msnos.usvc.api.RestApi;

@SuppressWarnings("restriction")
public class MiniHttpServer {

    public static final String URI_HEALTH = "/health";
    public static final String URI_MSNOS = "/msnos";

    public static final String URI_ADMIN_MESSAGES = "/admin/messages";

    private final HttpServer httpServer;
    private final RestApi[] apis;

    public MiniHttpServer(Cloud cloud, int port) throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.setExecutor(Executors.newCachedThreadPool());
        httpServer.createContext(URI_HEALTH, new HealthcheckHandler());
        httpServer.createContext(URI_MSNOS, new MsnosHandler(cloud));
        
        apis = new RestApi[] {
            new RestApi(URI_HEALTH, port).asHealthCheck(),
            new RestApi(URI_MSNOS, port).asMsnosEndpoint(),
        };
    }

    public void start() {
        httpServer.start();
    }

    public RestApi[] apis() {
        return apis;
    }

    public void stop() {
        httpServer.stop(0);
    }
}
