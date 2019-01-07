package com.telenordigital.nbiot;

import java.net.http.*;
import java.net.URI;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;

public class OutputStream implements WebSocket.Listener {
	private static final Logger logger = Logger.getLogger(OutputStream.class.getName());

	private Client.OutputHandler handler = null;
	private WebSocket webSocket = null;
	private String text = "";

	private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		= new com.fasterxml.jackson.databind.ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public OutputStream(URI uri, String token, Client.OutputHandler handler) {
		this.handler = handler;

		HttpClient client = HttpClient.newHttpClient();
		try {
			webSocket = client.newWebSocketBuilder()
				.header("X-API-Token", token)
				.buildAsync(uri, this)
				.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "").join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public CompletionStage<?> onText(WebSocket webSocket, CharSequence message, boolean last) {
		webSocket.request(1);

		text += message.toString();
		if (last) {
			String fullText = text;
			text = "";
			try {
				OutputDataMessage msg = jacksonObjectMapper.readValue(fullText, OutputDataMessage.class);
				if (msg.type().equals("data")) {
					handler.onData(msg);
				}
			} catch (java.io.IOException e) {
				logger.log(Level.WARNING, "Error parsing message: {0}", e.toString());
				logger.log(Level.WARNING, "Message was: {0}", text);
			}
		}

		return null;
	}

	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		handler.onEnd();
		return null;
	}

	public void onError(WebSocket webSocket, Throwable error) {
		logger.log(Level.WARNING, "{0}", error);
	}
}
