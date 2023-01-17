package Utilities;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


public final class JsonUtil {
	private static final Logger LOGGER = LogManager.getLogger();

	private JsonUtil() {

	}


	

	public static Map<String, String> getJsonAsMap(JsonObject dataSetObj) {
		Map<String, String> dataSetMap = new LinkedHashMap<>();
		for (Map.Entry<String, JsonElement> entry : dataSetObj.entrySet()) {
				dataSetMap.put(entry.getKey(), entry.getValue().getAsString());
			}
		
		return dataSetMap;
	}

	/**
	 * Read the json node for the given key in to a map. The json node should not
	 * have multiple nested nodes.
	 * 
	 * @param nodeName
	 * @return {@link Map}
	 */
	public static Map<String, String> getChildObjAsMap(JsonObject dataObject, String nodeName) {
		LOGGER.debug("reading all the child nodes of the json node: {} in to a map variable", nodeName);
		JsonObject dataSetElement = dataObject.get(nodeName).getAsJsonObject();
		Map<String, String> dataSetMap = getJsonAsMap(dataSetElement);
		LOGGER.trace("attribute map for the key: {} is : {}", nodeName, dataSetMap.toString());
		LOGGER.debug("reading of test data from the dataset {} to the map variable is complete", nodeName);
		return dataSetMap;
	}

	public static JsonObject getFileAsJson(String path) {
		try {
			return new Gson().fromJson(new BufferedReader(new FileReader(path)), JsonObject.class);
		} catch (IOException | JsonParseException e) {
			Assert.fail("Unable to read the json object from the file", e);
			return null;
		}
	}

	public static JsonObject getFirstJsonNode(String path) {
		try {
			return new Gson().fromJson(new BufferedReader(new FileReader(path)), JsonArray.class).get(0)
					.getAsJsonObject();
		} catch (IOException | JsonParseException e) {
			Assert.fail("Unable to read the json array from the file", e);
			return null;
		}

	}
}
