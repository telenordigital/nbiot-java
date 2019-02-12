package com.telenordigital.nbiot.OutputHandlers;

public interface CloseHandler {
	void handle(int statusCode, String reason);
}