package com.telenordigital.horde;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class OutputTest {
	@Test
	public void testOutput() throws Exception {
		// This test doesn't do much, but it can be handy for manual testing.

		Client client = new Client();
		Collection collection = client.createCollection(new ImmutableCollection.Builder().build());

		client.collectionOutput(collection.id(), new Client.OutputHandler() {
			@Override
			public void onOutput(OutputMessage msg) {
			}

			@Override
			public void onEnd() {
			}
		});
		// Thread.sleep(4000);

		client.deleteCollection(collection.id());
	}
}