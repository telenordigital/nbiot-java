package com.telenordigital.horde;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TeamTest {
	@Test
	public void testTeam() throws Exception {
		Client client = new Client();
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("name", "The test team");
		Team team = new ImmutableTeam.Builder().tags(tags).build();
		team = client.createTeam(team);
		client.updateTeam(team);
		client.deleteTeam(team.id());
	}
}