package com.weathermonitoring.controller;


import com.weathermonitoring.model.DailyWeatherSummary;
import com.weathermonitoring.model.WeatherData;
import com.weathermonitoring.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/latest")
@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // Endpoint to get the latest weather data for a city
    @GetMapping("/{city}")
    public ResponseEntity<WeatherData> getLatestWeatherData(@PathVariable String city) {
        WeatherData weatherData = weatherService.getLatestWeatherData(city);
        if (weatherData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Weather data not found for city: " + city);
        }
        return ResponseEntity.ok(weatherData);
    }

    // Endpoint to get daily weather summaries for a city
    @GetMapping("/daily-summaries/{city}")
    public List<DailyWeatherSummary> getDailySummariesForCity(@PathVariable String city) {
        return weatherService.getDailySummaries(city);
    }
}
