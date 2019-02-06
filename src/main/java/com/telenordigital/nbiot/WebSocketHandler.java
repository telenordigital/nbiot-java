package com.telenordigital.nbiot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Set up a WebSocket for output stream
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class WebSocketHandler {
	private static final Logger logger = Logger.getLogger(WebSocketHandler.class.getName());
	private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
			= new com.fasterxml.jackson.databind.ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@SuppressWarnings("unused")
	private Session session;

	OutputStreamHandler handler;

	public WebSocketHandler(OutputStreamHandler handler) {
		this.handler = handler;
	}

	@OnWebSocketClose
	public void onWebsocketClose(int statusCode, String reason) {
		if (this.handler.closeHandler != null) {
			this.handler.closeHandler.handle(statusCode, reason);
		} else {
			logger.log(Level.INFO, "Connection closed: \nStatus code:" + statusCode + "\nReason:" + reason);
		}
		this.session = null;
	}

	@OnWebSocketConnect
	public void onWebsocketConnect(Session session) {
		this.session = session;

		if (this.handler.connectHandler != null) {
			this.handler.connectHandler.handle(session);
		} else {
			logger.log(Level.INFO, "Connected with websocket");
		}
	}

	@OnWebSocketMessage
	public void onWebsocketMessage(String msg) {
		try {
			OutputDataMessageInternal msgInternal = jacksonObjectMapper.readValue(msg, OutputDataMessageInternal.class);

			if (msgInternal.type().equals("data")) {
				OutputDataMessage outputDataMessage = new ImmutableOutputDataMessage.Builder()
						.device(msgInternal.device())
						.payload(msgInternal.payload())
						.received(java.time.Instant.ofEpochMilli(msgInternal.received()))
						.build();
				if (this.handler.messageHandler != null) this.handler.messageHandler.handle(outputDataMessage);
			}
		} catch (java.io.IOException e) {
			logger.log(Level.WARNING, "Error parsing message", e);
			logger.log(Level.WARNING, "Message was: " + msg);
		}
	}

	@OnWebSocketError
	public void onWebsocketError(Throwable error) {
		if (this.handler.errorHandler != null) {
			this.handler.errorHandler.handle(this.session, error);
		} else {
			logger.log(Level.WARNING, "Websocket error", error);
		}
	}
}


