package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class OutputStreamTest {
	@Test
	public void testOutputStream() throws Exception {
		// This test doesn't do much, but it can be handy for manual testing.

		Client client = new Client();
		Collection collection = client.createCollection(new ImmutableCollection.Builder().build());

		try {
			OutputStream output = client.collectionOutput(collection.id(), new Client.OutputHandler() {
				@Override
				public void onData(OutputDataMessage msg) {
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