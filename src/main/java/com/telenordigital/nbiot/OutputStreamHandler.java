package com.telenordigital.nbiot;

import com.telenordigital.nbiot.OutputHandlers.CloseHandler;
import com.telenordigital.nbiot.OutputHandlers.ConnectHandler;
import com.telenordigital.nbiot.OutputHandlers.ErrorHandler;
import com.telenordigital.nbiot.OutputHandlers.MessageHandler;


/**
 * Set up a OutputStreamHandler for output stream
 */
public class OutputStreamHandler {
	protected MessageHandler messageHandler;
	protected ConnectHandler connectHandler;
	protected ErrorHandler errorHandler;
	protected CloseHandler closeHandler;

	public void onMessage(MessageHandler handler) {
		this.messageHandler = handler;
	}

	public void onConnect(ConnectHandler handler) {
		this.connectHandler = handler;
	}

	public void onError(ErrorHandler handler) {
		this.errorHandler = handler;
	}

	public void onClose(CloseHandler handler) {
		this.closeHandler = handler;
	}
}


