package com.telenordigital.horde;

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
	 * Invalid response from server.
	 */
	public ClientException(final String errorMessage, final int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
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