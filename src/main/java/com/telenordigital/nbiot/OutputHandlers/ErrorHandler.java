package com.telenordigital.nbiot.OutputHandlers;

import org.eclipse.jetty.websocket.api.Session;

public interface ErrorHandler {
	void handle(Session session, Throwable error);
}