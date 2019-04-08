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
			Map<String, Object> config = new HashMap<>();
			config.put("url", Config.DEFAULT_ENDPOINT);
			Output output = client.createOutput(collection.id(), new ImmutableOutput.Builder().type("webhook").config(config).build());
			try {
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