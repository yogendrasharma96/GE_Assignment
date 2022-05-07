package com.ge.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ge.project.service.WeatherService;

@RestController
@RequestMapping("/ge")
public class Controller {

	@Autowired
	WeatherService service;

	@GetMapping("/location/{place}")
	public ResponseEntity<JsonNode> getWeatherByLocation(@PathVariable String place) throws JsonMappingException, JsonProcessingException {
		JsonNode data = service.getWeatherOfLocation(place);
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/location/rainy/{place}")
	public ResponseEntity<String> getRainyWeatherByLocation(@PathVariable String place) throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(service.getRainyWeatherOfLocation(place));
	}
	
	@GetMapping("/location/sunny/{place}")
	public ResponseEntity<String> getSunnyWeatherByLocation(@PathVariable String place) throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(service.getSunnyWeatherOfLocation(place));
	}

}
