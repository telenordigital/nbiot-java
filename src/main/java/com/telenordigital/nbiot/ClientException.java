package com.telenordigital.nbiot;

import java.io.IOException;

import com.mashape.unirest.http.HttpResponse;

/**
 * Generic exception whenever something (tm) goes wrong.
 */
public class ClientException extends Exception {
	static final long serialVersionUID = 0;

	private final String errorMessage;
	private final int statusCode;

	/**
	 * Wrap a source exception.
	 */
	public ClientException(final Throwable t) {
		super(t);
		errorMessage = "Client exception";
		statusCode = 0;
	}

	/**
	 * Wrap an HTTP response.
	 */
	public ClientException(final HttpResponse<?> resp) {
		String msg = "";
		try {
			msg = new String(resp.getRawBody().readAllBytes());
		} catch(IOException x) {}
		this.errorMessage = msg;
		this.statusCode = resp.getStatus();
	}

	/**
	 * Invalid response from server.
	 */
	public ClientException(final String errorMessage, final int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return String.format("ClientException (status=%d): %s", statusCode, errorMessage);
	}

	/**
	 * Status code from server (if applicable)
	 */
	public int status() {
		return statusCode;
	}

	/**
	 * Error message from server (if applicable)
	 */
	public String message() {
		return errorMessage;
	}
}