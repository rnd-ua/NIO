package http.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.grizzly.threadpool.GrizzlyExecutorService;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

public class asyncMain {

	public static void main(String[] args) {
		HttpServer httpServer = new HttpServer();
		NetworkListener networkListener = new NetworkListener(
				"sample-listener", "0.0.0.0", 7000);

		// Configure NetworkListener thread pool to have just one thread,
		// so it would be easier to reproduce the problem
		ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig()
				.setCorePoolSize(10).setMaxPoolSize(200);

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
			final ExecutorService complexAppExecutorService = GrizzlyExecutorService.createInstance(ThreadPoolConfig
					.defaultConfig().copy().setCorePoolSize(100)
					.setMaxPoolSize(1600));

			@Override
			public void service(final Request request, final Response response)
					throws Exception {

				response.suspend(); // Instruct Grizzly to not flush response,
									// once we exit the service(...) method

				complexAppExecutorService.execute(new Runnable() { // Execute
																	// long-lasting
																	// task in
																	// the
																	// custom
																	// thread
							public void run() {
								try {
									response.setContentType("text/plain");
									// Simulate long lasting task
									Thread.sleep(200);
									response.getWriter().write(
											"Complex task is done!");
								} catch (Exception e) {
									response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
								} finally {
									response.resume(); // Finishing HTTP request
														// processing and
														// flushing the response
														// to the client
								}
							}
						});
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
