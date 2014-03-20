package websocket.grizzly.server;

import java.io.IOException;

import org.glassfish.grizzly.http.server.AddOn;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;

public class ServerMain {
	public static void main(String [] args) throws IOException {
        // create a Grizzly HttpServer to server static resources from 'webapp', on PORT.
        final HttpServer server = HttpServer.createSimpleServer("/var/www", 7000);

        // Register the WebSockets add on with the HttpServer
        NetworkListener listener = server.getListener("grizzly");
//        listener.registerAddOn(new WebSocketAddOn());
        
        WebSocketAddOn addon= new WebSocketAddOn();
        addon.setTimeoutInSeconds(-1);
        listener.registerAddOn(addon);

        // initialize websocket chat application
        final WebSocketApplication chatApplication = new ChatApplication();

        // register the application
        WebSocketEngine.getEngine().register("/grizzly-websockets-chat", "/chat", chatApplication);

        try {
            server.start();
            System.out.println("Press any key to shutdownNow the server...");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } finally {
            // shutdownNow the server
            server.shutdownNow();
        }
	}
}
