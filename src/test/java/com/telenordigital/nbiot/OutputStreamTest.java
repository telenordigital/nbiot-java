package com.telenordigital.nbiot;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

public class OutputStreamTest {
	private static final Logger logger = Logger.getLogger(OutputStreamTest.class.getName());

	@Test
	public void testOutputStream() throws Exception {
		// This test doesn't do much, but it can be handy for manual testing.

		Client client = new Client();
		Collection collection = client.createCollection(new ImmutableCollection.Builder().build());

		try {
			WebSocketClient wsClient = client.collectionOutput(collection.id(), handler -> {
				handler.onConnect((session -> logger.info("Handler connect")));
				handler.onError(((session, error) -> logger.warning("Handler error")));
				handler.onClose((code, reason) -> logger.info("Handler close"));
				handler.onMessage((message) -> {
					logger.info("Handler message");
					logger.info(message.toString());
				});
			});

			new CountDownLatch(1).await(2, TimeUnit.SECONDS);

			wsClient.stop();
		} catch (Exception ex) {
			//
		} finally {
			client.deleteCollection(collection.id());
		}
	}
}