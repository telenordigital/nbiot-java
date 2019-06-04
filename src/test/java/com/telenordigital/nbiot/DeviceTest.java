package com.telenordigital.nbiot;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;

public class DeviceTest {
	@Test
	public void testDevice() throws Exception {
		Client client = new Client();
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("name", "The test collection");
		Collection collection = client.createCollection(new ImmutableCollection.Builder().tags(tags).build());

		Random rand = new Random();

		try {
			List<Device> devices = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				String imei = String.valueOf(rand.nextInt((int)1e15));
				String imsi = String.valueOf(rand.nextInt((int)1e15));
				Device device = new ImmutableDevice.Builder().imei(imei).imsi(imsi).build();
				device = client.createDevice(collection.id(), device);
				devices.add(device);
			}

			client.devices(collection.id());

			for (Device d : devices) {
				try {
					client.data(collection.id(), d.id());
				} finally {
					client.deleteDevice(collection.id(), d.id());
				}
			}
		} finally {
			client.deleteCollection(collection.id());
		}
	}
}