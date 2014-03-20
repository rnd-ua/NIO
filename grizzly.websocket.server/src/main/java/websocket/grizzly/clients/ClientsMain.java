package websocket.grizzly.clients;

import com.ning.http.client.*;
import com.ning.http.client.providers.grizzly.GrizzlyAsyncHttpProvider;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ClientsMain {

	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException {
	
		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().setConnectionTimeoutInMs(-1).
				   														   setIdleConnectionInPoolTimeoutInMs(-1).
				   														   setIdleConnectionTimeoutInMs(-1).
				   														   setRequestTimeoutInMs(-1).
				   														   setWebSocketIdleTimeoutInMs(-1).build();
		AsyncHttpClient client = new AsyncHttpClient(new GrizzlyAsyncHttpProvider(config), config);
		Integer count = Integer.valueOf(args[0]);
		for(int i=0; i< count; i++) {
			send(i, config, client);
		}
	}
	
	private static void send(final Integer index, AsyncHttpClientConfig _config, AsyncHttpClient _client) throws InterruptedException, ExecutionException, IOException {
		// https://github.com/AsyncHttpClient/async-http-client
		// http://stackoverflow.com/questions/16152648/websocket-plugin-for-jmeter

		WebSocket webSocket = _client
				.prepareConnect(
						"ws://54.203.251.40:7000/grizzly-websockets-chat/chat")
				.execute(
						new WebSocketUpgradeHandler.Builder()
								.addWebSocketListener(
										new WebSocketTextListener() {
											public void onOpen(
													WebSocket webSocket) {
												// LOGGER.info("Connected: " +
												// webSocket);
//												System.out
//														.println("Connected: " + index);
											}

											public void onClose(
													WebSocket webSocket) {
												// LOGGER.info("Closed: " +
												// webSocket);
//												System.out.println("Closed: "
//														+ webSocket);
											}

											public void onError(
													Throwable throwable) {
												// LOGGER.severe("Error: " +
												// throwable);
												System.out.println("Error: "
														+ throwable);
											}

											// This will be invoked upon each
											// broadcast from Atmosphere on the
											// server
											// side.
											public void onMessage(final String s) {

//												System.out
//														.println("onMessage: "
//																+ index
//																+ "  " + s);
											}

											public void onFragment(String s,
													boolean b) {
											}
										}).build()).get();

		webSocket.sendTextMessage("login: ");
		webSocket.sendTextMessage("message sent");
	}
}
