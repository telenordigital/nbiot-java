package com.telenordigital.nbiot;

import org.apache.http.HttpStatus;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class DownstreamTest {
	@Test
	public void testDownstream() throws Exception {
		// Sending downstream messages is very slow because we expect these operations to time out (because these devices don't really exist).
		// So we only run these tests during continuous integration.  Comment out the next line to run them locally.
		assumeTrue(System.getenv("CI") == "true");

		Client client = new Client();
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("name", "The test collection");
		Collection collection = client.createCollection(new ImmutableCollection.Builder().tags(tags).build());

		Random rand = new Random();
		try {
			List<Device> devices = new ArrayList<>();
			try {
				for (int i = 0; i < 5; i++) {
					String imei = Integer.toString(rand.nextInt((int)1e15));
					String imsi = Integer.toString(rand.nextInt((int)1e15));
					Device device = new ImmutableDevice.Builder().imei(imei).imsi(imsi).build();
					device = client.createDevice(collection.id(), device);
					devices.add(device);
				}
				
				ClientException ex = assertThrows(ClientException.class, new ThrowingRunnable(){
					@Override
					public void run() throws Throwable {
						client.send(collection.id(), devices.get(0).id(), new ImmutableDownstreamMessage.Builder().port(1234).payload("Hello, device!".getBytes()).build());
					}
				});
				assertEquals(HttpStatus.SC_CONFLICT, ex.status());

				BroadcastResult res = client.broadcast(collection.id(), new ImmutableDownstreamMessage.Builder().port(1234).payload("Hello, device!".getBytes()).build());
				assertEquals(devices.size(), res.failed());
			} finally {
				for (Device d : devices) {
					client.deleteDevice(collection.id(), d.id());
				}
			}
		} finally {
			client.deleteCollection(collection.id());
		}
	}
}