package com.telenordigital.nbiot.OutputHandlers;

import org.eclipse.jetty.websocket.api.Session;

public interface ConnectHandler {
	/**
	 * Handle function on WebSocket connect
	 *
	 * @param session The WebSocket session after connect
	 */
	void handle(Session session);
}