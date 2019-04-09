package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class OutputTest {
	@Test
	public void testOutput() throws Exception {
		Client client = new Client();
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("name", "The test collection");
		Collection collection = new ImmutableCollection.Builder().tags(tags).build();
		collection = client.createCollection(collection);

		try {
			Output output = new ImmutableWebHookOutput.Builder().url(Config.DEFAULT_ENDPOINT).build();
			output = client.createOutput(collection.id(), output);
			try {
				client.updateOutput(collection.id(), output);
				client.outputLogs(collection.id(), output.id());
				client.outputStatus(collection.id(), output.id());
			} finally {
				client.deleteOutput(collection.id(), output.id());
			}
		} finally {
			client.deleteCollection(collection.id());
		}
	}
}