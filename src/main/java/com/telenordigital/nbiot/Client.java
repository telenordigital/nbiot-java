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
				throw new ClientException(resp.getBody(), resp.getStatus());
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
			return Unirest
					.get(endpoint + path)
					.header(TOKEN_HEADER, token)
					.asObject(cls).getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	private OutputDataMessageInternal.OutputDataMessageListInternal getData(final String path, DataSearchParameters parameters)
			throws ClientException {
		try {
			Instant since = parameters.since();
			Instant until = parameters.until();
			return Unirest
					.get(endpoint + path)
					.header(TOKEN_HEADER, token)
					.queryString("limit", parameters.limit())
					.queryString("since", since != null ? since.toEpochMilli() : null)
					.queryString("until", until != null ? until.toEpochMilli() : null)
					.asObject(OutputDataMessageInternal.OutputDataMessageListInternal.class).getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	/**
	 * generic POST on a resource
	 */
	private <T> T create(
			final String path, final T template, final Class<T> cls)
			throws ClientException {
		try {
			return Unirest
					.post(endpoint + path)
					.header(TOKEN_HEADER, token)
					.body(template)
					.asObject(cls)
					.getBody();
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
			return Unirest
					.patch(endpoint + path)
					.header(TOKEN_HEADER, token)
					.body(updated)
					.asObject(cls)
					.getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	/**
	 * generic DELETE on a resource
	 */
	private void delete(final String path) throws ClientException {
		try {
			Unirest.delete(endpoint + path)
					.header(TOKEN_HEADER, token)
					.asString();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}

	/**
	 * Retrieve a team.
	 */
	public Team team(final String teamID)
			throws ClientException {
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
	public Team createTeam(final Team template) throws ClientException {
		return create("/teams", template, Team.class);
	}

	/**
	 * Update a team.
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
	public Collection createCollection(final Collection template) throws ClientException {
		return create("/collections", template, Collection.class);
	}

	/**
	 * Update a collection.
	 */
	public Collection updateCollection(final Collection collection) throws ClientException {
		return update("/collections/" + collection.id(), collection, Collection.class);
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
	public Device createDevice(final String collectionID, final Device template) throws ClientException {
		return create("/collections/" + collectionID + "/devices", template, Device.class);
	}

	/**
	 * Update a device.
	 */
	public Device updateDevice(final String collectionID, final Device device) throws ClientException {
		return update("/collections/" + collectionID + "/devices/" + device.id(), device, Device.class);
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
	public Output createOutput(final String collectionID, final Output template) throws ClientException {
		return create("/collections/" + collectionID + "/outputs", template, Output.class);
	}

	/**
	 * Update an output.
	 */
	public Output updateOutput(final String collectionID, final Output output) throws ClientException {
		return update("/collections/" + collectionID + "/outputs/" + output.id(), output, Output.class);
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
	public WebSocketClient collectionOutput(final String collectionID, Consumer<OutputStreamHandler> handler) {
		return initiateDataMessageStream("/collections/" + collectionID, handler);
	}

	/**
	 * Receive data messages sent by a device.
	 */
	public WebSocketClient deviceOutput(final String collectionID, final String deviceID, Consumer<OutputStreamHandler> handler) {
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
