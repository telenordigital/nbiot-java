package com.telenordigital.nbiot.OutputHandlers;

import com.telenordigital.nbiot.OutputDataMessage;

public interface MessageHandler {
	void handle(OutputDataMessage outputDataMessage);
}