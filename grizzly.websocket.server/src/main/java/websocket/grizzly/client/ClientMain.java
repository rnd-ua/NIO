package websocket.grizzly.client;

//import com.ning.http.client.AsyncHttpClient;
//import com.ning.http.client.AsyncHttpClientConfig;
//import com.ning.http.client.providers.grizzly.GrizzlyAsyncHttpProvider;
//import com.ning.http.client.websocket.DefaultWebSocketListener;
//import com.ning.http.client.websocket.WebSocket;
//import com.ning.http.client.websocket.WebSocketListener;
//import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import com.ning.http.client.*;
import com.ning.http.client.providers.grizzly.GrizzlyAsyncHttpProvider;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ClientMain {

	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException {
		// http://www.notshabby.net/2012/01/async-http-client-1-7-0-released-details-on-the-grizzly-side-of-things/

		// This example will send a WS message to a server that echoes the
		// message back
		// AsyncHttpClientConfig config = new
		// AsyncHttpClientConfig.Builder().build();
		// AsyncHttpClient c = new AsyncHttpClient(new
		// GrizzlyAsyncHttpProvider(config), config));
		// String wsUrl = "ws://somehost/wspath";
		// WebSocketListener listener = new DefaultWebSocketListener() {
		// @Override
		// void onMessage(String message) {
		// System.out.println("Received message: " + message);
		// }
		// };
		// WebSocketUpgradeHandler handler = new
		// WebSocketUpgradeHandler().Builder().addWebSocketListener(listener).build();
		// WebSocket socket = c.prepareGet(wsUrl).execute(handler).get();
		// socket.sendMessage("Hello!".getBytes("UTF-8"));

		// https://github.com/AsyncHttpClient/async-http-client
		// http://stackoverflow.com/questions/16152648/websocket-plugin-for-jmeter

		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().setConnectionTimeoutInMs(-1).
																		   setIdleConnectionInPoolTimeoutInMs(-1).
																		   setIdleConnectionTimeoutInMs(-1).
																		   setRequestTimeoutInMs(-1).
																		   setWebSocketIdleTimeoutInMs(-1).
																		   build();
		AsyncHttpClient client = new AsyncHttpClient(new GrizzlyAsyncHttpProvider(config), config);
		WebSocket webSocket = client
				.prepareConnect(
						"ws://54.203.251.40:7000/grizzly-websockets-chat/chat")
//						"ws://localhost:7000/grizzly-websockets-chat/chat")
				.execute(
						new WebSocketUpgradeHandler.Builder()
								.addWebSocketListener(
										new WebSocketTextListener() {
											public void onOpen(
													WebSocket webSocket) {
												// LOGGER.info("Connected: " +
												// webSocket);
												System.out
														.println("Connected: "
																+ webSocket + " at:" + new Date());
											}

											public void onClose(
													WebSocket webSocket) {
												// LOGGER.info("Closed: " +
												// webSocket);
												System.out.println("Closed: "
														+ webSocket + " at:" + new Date());
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
											int counter = 0;

											public void onMessage(final String s) {

												System.out
														.println("onMessage: "
																+ counter++
																+ "  " + s);
												// All IO is performed on a
												// background thread - UI
												// updates from said thread
												// must be performed using the
												// runOnUiThread() call.
												// runOnUiThread(new Runnable()
												// {
												// public void run() {
												// try {
												// // Message will be json, so
												// use Android's built-in json
												// support
												// JSONObject obj = new
												// JSONObject(s);
												// textView.append((obj.get("author")
												// + ": ";
												// + obj.get("text")
												// + '\n'));
												// } catch (Exception e) {
												// onError(e);
												// }
												// }
												// });
											}

											public void onFragment(String s,
													boolean b) {
											}
										}).build()).get();

		webSocket.sendTextMessage("login: ");
//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		for (;;) {
//			String line = in.readLine();
//			if (line == null) {
//				break;
//			}
//
//			webSocket.sendTextMessage(line);
//		}
//		for (int i = 0; i < 20; i++) {
//			webSocket.sendTextMessage("message " + i);
//		}
	}
}
