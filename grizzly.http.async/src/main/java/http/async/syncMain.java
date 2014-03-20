package http.async;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

public class syncMain {

	public static void main(String[] args) {
		HttpServer httpServer = new HttpServer();
		NetworkListener networkListener = new NetworkListener(
				"sample-listener", "0.0.0.0", 7000);

		// Configure NetworkListener thread pool to have just one thread,
		// so it would be easier to reproduce the problem
		ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig()
				.setCorePoolSize(1).setMaxPoolSize(10);

		networkListener.getTransport().setWorkerThreadPoolConfig(
				threadPoolConfig);

		httpServer.addListener(networkListener);

		httpServer.getServerConfiguration().addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response)
					throws Exception {
				response.setContentType("text/plain");
				response.getWriter().write("Simple task is done!");
			}
		}, "/simple");

		httpServer.getServerConfiguration().addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response)
					throws Exception {
				response.setContentType("text/plain");
				// Simulate long lasting task
				Thread.sleep(1000);
				response.getWriter().write("Complex task is done!");
			}
		}, "/complex");

		try {
			// server.start();
			httpServer.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
