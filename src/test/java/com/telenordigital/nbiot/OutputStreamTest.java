package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import org.junit.Test;

public class OutputStreamTest {
	private static final Logger logger = Logger.getLogger(OutputStreamTest.class.getName());

	@Test
	public void testOutputStream() throws Exception {
		// This test doesn't do much, but it can be handy for manual testing.

		Client client = new Client();
		Collection collection = client.createCollection(new ImmutableCollection.Builder().build());

		try {
			OutputStream output = client.collectionOutput(collection.id(), new Client.OutputHandler() {
				@Override
				public void onData(OutputDataMessage msg) {
					logger.log(Level.INFO, "{0}", msg);
				}

				@Override
				public void onEnd() {
				}
			});
			output.close();
		} finally {
			client.deleteCollection(collection.id());
		}
	}
}