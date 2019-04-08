package com.telenordigital.nbiot;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.http.HttpStatus;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 * This is the client for the Telenor NB-IoT REST API.
 */
public class Client {
	private static final Logger logger = Logger.getLogger(Client.class.getName());

	private static final String TOKEN_HEADER = "X-API-Token";

	private final String endpoint;
	private final String token;

	/**
	 * Default data search parameters.
	 *
	 * Used if no search parameters is provided in {@link #data(String, String)} and {@link #data(String)}
	 */
	public final DataSearchParameters DEFAULT_DATA_SEARCH_PARAMS = new ImmutableDataSearchParameters.Builder()
			.limit(255)
			.since(null)
			.until(null)
			.build();

	static {
		Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
					= new com.fasterxml.jackson.databind.ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * Create a new Telenor NB-IoT client using the configuration. The configuration
	 * can either be set by adding a ${HOME}/.telenor-nbiot file or by setting the
	 * environment variables TELENOR_NBIOT_ADDRESS and TELENOR_NBIOT_TOKEN. The environment
	 * variables override the configuration file.
	 */
	public Client() throws ClientException {
		final Config cfg = new Config();
		this.token = cfg.token();
		this.endpoint = cfg.endpoint();

		ping();
	}

	/**
	 * Create a new Telenor NB-IoT client with the specified endpoint and token.
	 */
	public Client(final String endpoint, final String token) {
		this.endpoint = endpoint;
		this.token = token;
	}

	/**
	 * Ping the backend to ensure it is reachable
	 */
	private void ping() throws ClientException {
		try {
			final HttpResponse<String> resp = Unirest
					.get(endpoint + "/")
					.header(TOKEN_HEADER, token)
					.asString();
			if (resp.getStatus() == HttpStatus.SC_FORBIDDEN) {
				throw new ClientException(resp);
			}

		} catch (UnirestException ex) {
			throw new ClientException(ex);
		}
	}

	/**
	 * generic GET on a resource
	 */
	private <T> T get(final String path, final Class<T> cls)
			throws ClientException {
		try {
			final HttpResponse<T> resp = Unirest
					.get(endpoint + path)
					.header(TOKEN_HEADER, token)
					.asObject(cls);
			if (resp.getStatus() >= 300) {
				throw new ClientException(resp);
			}
			return resp.getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	private OutputDataMessageInternal.OutputDataMessageListInternal getData(final String path, DataSearchParameters parameters)
			throws ClientException {
		try {
			Instant since = parameters.since();
			Instant until = parameters.until();
			final HttpResponse<OutputDataMessageInternal.OutputDataMessageListInternal> resp = Unirest
					.get(endpoint + path)
					.header(TOKEN_HEADER, token)
					.queryString("limit", parameters.limit())
					.queryString("since", since != null ? since.toEpochMilli() : null)
					.queryString("until", until != null ? until.toEpochMilli() : null)
					.asObject(OutputDataMessageInternal.OutputDataMessageListInternal.class);
			if (resp.getStatus() >= 300) {
				throw new ClientException(resp);
			}
			return resp.getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	/**
	 * generic POST on a resource
	 */
	private <T, U> U create(final String path, final T template, final Class<U> cls)
			throws ClientException {
		try {
			final HttpResponse<U> resp = Unirest
					.post(endpoint + path)
					.header(TOKEN_HEADER, token)
					.body(template)
					.asObject(cls);
			if (resp.getStatus() >= 300) {
				throw new ClientException(resp);
			}
			return resp.getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	/**
	 * generic PATCH on a resource
	 */
	private <T> T update(final String path, T updated, final Class<T> cls)
			throws ClientException {
		try {
			final HttpResponse<T> resp = Unirest
					.patch(endpoint + path)
					.header(TOKEN_HEADER, token)
					.body(updated)
					.asObject(cls);
			if (resp.getStatus() >= 300) {
				throw new ClientException(resp);
			}
			return resp.getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	/**
	 * generic DELETE on a resource
	 */
	private void delete(final String path) throws ClientException {
		try {
			final HttpResponse<String> resp = Unirest
					.delete(endpoint + path)
					.header(TOKEN_HEADER, token)
					.asString();
			if (resp.getStatus() >= 300) {
				throw new ClientException(resp);
			}
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}


	/**
	 * Retrieve the system configuration
	 */
	public SystemConfig systemConfig() throws ClientException {
		return get("/system", SystemConfig.class);
	}

	/**
	 * Retrieve a team.
	 */
	public Team team(final String teamID) throws ClientException {
		return get("/teams/" + teamID, Team.class);
	}

	/**
	 * Retrieve all teams the user has access to.
	 */
	public Team[] teams() throws ClientException {
		return get("/teams", Team.TeamList.class).teams();
	}

	/**
	 * Create a new team.
	 */
	public Team createTeam(final Team team) throws ClientException {
		return create("/teams", team, Team.class);
	}

	/**
	 * Update a team member role.
	 */
	public Member updateTeamMemberRole(final String teamID, final String userID, final String role) throws ClientException {
		Member member = new ImmutableMember.Builder().userID(userID).build();
		return update(String.format("/teams/%s/members/%s", teamID, userID), member, Member.class);
	}

	/**
	 * Delete a team member.
	 */
	public void deleteTeamMember(final String teamID, final String userID) throws ClientException {
		delete(String.format("/teams/%s/members/%s", teamID, userID));
	}

	/**
	 * Delete a team tag.
	 */
	public void deleteTeamTag(final String teamID, final String key) throws ClientException {
		delete(String.format("/teams/%s/tags/%s", teamID, key));
	}

	/**
	 * Update a team.
	 * No tags are deleted, only added or updated.
	 */
	public Team updateTeam(final Team team) throws ClientException {
		return update("/teams/" + team.id(), team, Team.class);
	}

	/**
	 * Delete a team.
	 */
	public void deleteTeam(final String teamID) throws ClientException {
		delete("/teams/" + teamID);
	}


	/**
	 * Retrieve an invite.
	 */
	public Invite invite(final String teamID, final String code) throws ClientException {
		return get(String.format("/teams/%s/invites/%s", teamID, code), Invite.class);
	}

	/**
	 * Retrieve all invites to a team.
	 */
	public Invite[] invites(final String teamID) throws ClientException {
		return get(String.format("/teams/%s/invites", teamID), Invite.InviteList.class).invites();
	}

	/**
	 * Create a new invite.
	 */
	public Invite createInvite(final String teamID) throws ClientException {
		return create(String.format("/teams/%s/invites", teamID), null, Invite.class);
	}

	/**
	 * Accept an invite.
	 */
	public Team acceptInvite(final String code) throws ClientException {
		Invite invite = new ImmutableInvite.Builder().code(code).createdAt(0).build();
		return create("/teams/accept", invite, Team.class);
	}

	/**
	 * Delete an invite.
	 */
	public void deleteInvite(final String teamID, final String code) throws ClientException {
		delete(String.format("/teams/%s/invites/%s", teamID, code));
	}


	/**
	 * Retrieve a collection.
	 */
	public Collection collection(final String collectionID) throws ClientException {
		return get("/collections/" + collectionID, Collection.class);
	}

	/**
	 * Retrieve all collections the user has access to.
	 */
	public Collection[] collections() throws ClientException {
		return get("/collections", Collection.CollectionList.class).collections();
	}

	/**
	 * Create a new collection.
	 */
	public Collection createCollection(final Collection collection) throws ClientException {
		return create("/collections", collection, Collection.class);
	}

	/**
	 * Update a collection.
	 * No tags are deleted, only added or updated.
	 */
	public Collection updateCollection(final Collection collection) throws ClientException {
		return update("/collections/" + collection.id(), collection, Collection.class);
	}

	/**
	 * Delete a collection tag.
	 */
	public void deleteCollectionTag(final String collectionID, final String key) throws ClientException {
		delete(String.format("/collections/%s/tags/%s", collectionID, key));
	}

	/**
	 * Delete a collection.
	 */
	public void deleteCollection(final String collectionID) throws ClientException {
		delete("/collections/" + collectionID);
	}

	/**
	 * Fetch data for a collection
	 *
	 * @param collectionID Collection id as string
	 * @return A list of OutputDataMessage
	 * @throws ClientException
	 */
	public OutputDataMessage[] data(final String collectionID) throws ClientException {
		OutputDataMessageInternal[] internalDataMessageList = getData(
				"/collections/" + collectionID + "/data",
				DEFAULT_DATA_SEARCH_PARAMS
		).messages();

		return mapDataMessagesFromInternal(internalDataMessageList);
	}

	/**
	 * Fetch data for a collection
	 *
	 * @param collectionID Collection id as string
	 * @param parameters   Map of DataSearchParameters
	 * @return A list of OutputDataMessage
	 * @throws ClientException
	 */
	public OutputDataMessage[] data(final String collectionID, DataSearchParameters parameters) throws ClientException {
		OutputDataMessageInternal[] internalDataMessageList = getData(
				"/collections/" + collectionID + "/data",
				parameters
		).messages();

		return mapDataMessagesFromInternal(internalDataMessageList);
	}

	/**
	 * Broadcast a messsage to all devices in a collection.
	 */
	public BroadcastResult broadcast(final String collectionID, final DownstreamMessage msg) throws ClientException {
		return create(String.format("/collections/%s/to", collectionID), msg, BroadcastResult.class);
	}


	/**
	 * Retrieve a device.
	 */
	public Device device(final String collectionID, final String deviceID) throws ClientException {
		return get("/collections/" + collectionID + "/devices/" + deviceID, Device.class);
	}

	/**
	 * Retrieve all devices in a collection.
	 */
	public Device[] devices(final String collectionID) throws ClientException {
		return get("/collections/" + collectionID + "/devices", Device.DeviceList.class).devices();
	}

	/**
	 * Create a device.
	 */
	public Device createDevice(final String collectionID, final Device device) throws ClientException {
		return create("/collections/" + collectionID + "/devices", device, Device.class);
	}

	/**
	 * Update a device.
	 * No tags are deleted, only added or updated.
	 */
	public Device updateDevice(final String collectionID, final Device device) throws ClientException {
		return update("/collections/" + collectionID + "/devices/" + device.id(), device, Device.class);
	}

	/**
	 * Delete a device tag.
	 */
	public void deleteDeviceTag(final String collectionID, final String deviceID, final String key) throws ClientException {
		delete(String.format("/collections/%s/devices/%s/tags/%s", collectionID, deviceID, key));
	}

	/**
	 * Delete a device.
	 */
	public void deleteDevice(final String collectionID, final String deviceID) throws ClientException {
		delete("/collections/" + collectionID + "/devices/" + deviceID);
	}

	/**
	 * Fetch data for a device
	 *
	 * @param collectionID Collection id as string
	 * @param deviceId     Device id as string
	 * @return A list of OutputDataMessage
	 * @throws ClientException
	 */
	public OutputDataMessage[] data(final String collectionID, final String deviceId) throws ClientException {
		OutputDataMessageInternal[] internalDataMessageList = getData(
				"/collections/" + collectionID + "/devices/" + deviceId + "/data",
				DEFAULT_DATA_SEARCH_PARAMS
		).messages();

		return mapDataMessagesFromInternal(internalDataMessageList);
	}

	/**
	 * Fetch data for a device
	 *
	 * @param collectionID Collection id as string
	 * @param deviceId     Device id as string
	 * @param parameters   Map of DataSearchParameters
	 * @return A list of OutputDataMessage
	 * @throws ClientException
	 */
	public OutputDataMessage[] data(final String collectionID, final String deviceId, DataSearchParameters parameters) throws ClientException {
		OutputDataMessageInternal[] internalDataMessageList = getData(
				"/collections/" + collectionID + "/devices/" + deviceId + "/data",
				parameters
		).messages();

		return mapDataMessagesFromInternal(internalDataMessageList);
	}

	/**
	 * Send a messsage to a device.
	 */
	public void send(final String collectionID, final String deviceID, final DownstreamMessage msg) throws ClientException {
		create(String.format("/collections/%s/devices/%s/to", collectionID, deviceID), msg, Object.class);
	}


	/**
	 * Retrieve an output.
	 */
	public Output output(final String collectionID, final String outputID) throws ClientException {
		return get("/collections/" + collectionID + "/outputs/" + outputID, Output.class);
	}

	/**
	 * Retrieve all outputs in a collection.
	 */
	public Output[] outputs(final String collectionID) throws ClientException {
		return get("/collections/" + collectionID + "/outputs", Output.OutputList.class).outputs();
	}

	/**
	 * Create an output.
	 */
	public Output createOutput(final String collectionID, final Output output) throws ClientException {
		return create("/collections/" + collectionID + "/outputs", output, Output.class);
	}

	/**
	 * Update an output.
	 * No tags are deleted, only added or updated.
	 */
	public Output updateOutput(final String collectionID, final Output output) throws ClientException {
		return update("/collections/" + collectionID + "/outputs/" + output.id(), output, Output.class);
	}

	/**
	 * Delete a output tag.
	 */
	public void deleteOutputTag(final String collectionID, final String outputID, final String key) throws ClientException {
		delete(String.format("/collections/%s/outputs/%s/tags/%s", collectionID, outputID, key));
	}

	/**
	 * Delete an output.
	 */
	public void deleteOutput(final String collectionID, final String outputID) throws ClientException {
		delete("/collections/" + collectionID + "/outputs/" + outputID);
	}


	/**
	 * Receive data messages sent by all devices in a collection.
	 */
	public WebSocketClient outputStream(final String collectionID, Consumer<OutputStreamHandler> handler) {
		return initiateDataMessageStream("/collections/" + collectionID, handler);
	}

	/**
	 * Receive data messages sent by a device.
	 */
	public WebSocketClient outputStream(final String collectionID, final String deviceID, Consumer<OutputStreamHandler> handler) {
		return initiateDataMessageStream("/collections/" + collectionID + "/devices/" + deviceID, handler);
	}

	private WebSocketClient initiateDataMessageStream(final String path, Consumer<OutputStreamHandler> handler) {

		WebSocketClient client = new WebSocketClient();
		client.getSslContextFactory().setEndpointIdentificationAlgorithm("HTTPS");
		OutputStreamHandler outputStreamHandler = new OutputStreamHandler();

		try {
			client.start();

			URI endpointURI = new URI(endpoint);

			String scheme = "wss";
			if (endpointURI.getScheme().equals("http")) {
				scheme = "ws";
			}
			URI uri = new URI(
					scheme,
					null,
					endpointURI.getHost(),
					endpointURI.getPort(),
					path + "/from",
					null,
					null
			);

			ClientUpgradeRequest request = new ClientUpgradeRequest();
			request.setHeader(TOKEN_HEADER, token);
			handler.accept(outputStreamHandler);

			client.connect(new WebSocketHandler(outputStreamHandler), uri, request);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to set up websocket.", e);
		}

		return client;
	}

	private OutputDataMessage[] mapDataMessagesFromInternal(OutputDataMessageInternal[] internalMessages) {
		return Stream.of(internalMessages).map(internalDataMessage ->
				new ImmutableOutputDataMessage.Builder()
						.device(internalDataMessage.device())
						.payload(internalDataMessage.payload())
						.received(java.time.Instant.ofEpochMilli(internalDataMessage.received()))
						.build()
		).toArray(OutputDataMessage[]::new);
	}
}
