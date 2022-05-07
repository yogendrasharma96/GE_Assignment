package com.ge.project.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ge.project.util.RestConstants;

@Service
public class WeatherService {

	@Value("${location.api}")
	private String baseUrl;

	@Autowired
	private RestTemplate rest;

	@Autowired
	private ObjectMapper mapper;

	private ResponseEntity<String> response = null;

	private JsonNode root = null;

	public JsonNode getWeatherOfLocation(String place) throws JsonMappingException, JsonProcessingException {
		response = rest.getForEntity(baseUrl + place, String.class);
			root = mapper.readTree(response.getBody());
		ObjectNode node = (ObjectNode) root.get(RestConstants.CURRENT_CONDITIONS);
		node.remove(RestConstants.ICON_URL);
		return node;
	}

	public String getRainyWeatherOfLocation(String place) throws JsonMappingException, JsonProcessingException {

		Entry<Integer, Integer> entry = getPrediction(place).entrySet().iterator().next();
		int value = entry.getValue();
		return mapper.convertValue(LocalDate.now().plusDays(value - 1).toString(), String.class);
	}

	private NavigableMap<Integer, Integer> getPrediction(String place) throws JsonMappingException, JsonProcessingException {
		NavigableMap<Integer, Integer> map = new TreeMap<>();
		response = rest.getForEntity(baseUrl + place, String.class);
			root = mapper.readTree(response.getBody());
		ArrayNode node = (ArrayNode) root.get(RestConstants.NEXT_DAYS);
		int index = 0;
		if (node.isArray()) {
			for (JsonNode jsonNode : node) {
				index++;
				String typeOfweather = jsonNode.get(RestConstants.COMMENT).asText();
				if (getRainMap().containsKey(typeOfweather)) {
					if (!map.containsKey(getRainMap().get(typeOfweather)))
						map.put(getRainMap().get(typeOfweather), index);
				}
			}
		}
		return map;
	}

	private HashMap<String, Integer> getRainMap() {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("Rain", 1);
		map.put("Showers", 2);
		map.put("Mostly cloudy", 3);
		map.put("Cloudy", 4);
		map.put("Partly cloudy", 5);
		map.put("Sunny", 6);
		map.put("Clear", 7);
		return map;

	}

	public String getSunnyWeatherOfLocation(String place) throws JsonMappingException, JsonProcessingException {
		Entry<Integer, Integer> entry = getPrediction(place).lastEntry();
		int value = entry.getValue();
		return mapper.convertValue(LocalDate.now().plusDays(value - 1).toString(), String.class);

	}

}
