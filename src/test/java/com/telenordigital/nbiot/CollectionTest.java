package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class CollectionTest {
	@Test
	public void testCollection() throws Exception {
		Client client = new Client();
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("name", "The test collection");
		Collection collection = new ImmutableCollection.Builder().tags(tags).build();
		collection = client.createCollection(collection);

		client.collections();

		try {
			client.data(collection.id(), new ImmutableDataSearchParameters.Builder().build());
			client.updateCollection(collection);
		} finally {
			client.deleteCollection(collection.id());
		}
	}
}