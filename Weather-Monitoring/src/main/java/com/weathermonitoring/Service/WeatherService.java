package com.weathermonitoring.Service;



import com.weathermonitoring.Repository.DailyWeatherSummaryRepository;
import com.weathermonitoring.Repository.WeatherDataRepository;
import com.weathermonitoring.model.DailyWeatherSummary;
import com.weathermonitoring.model.WeatherData;
import com.weathermonitoring.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {
    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private DailyWeatherSummaryRepository summaryRepository;

    @Value("8bb7f7ed9ec1a7058631be0ca49f1d57") // Ensure this matches your application properties
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void fetchWeatherData(String city) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
                city, apiKey);

        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response != null) {
            WeatherData data = new WeatherData();
            data.setCity(city);
            data.setTemperature(response.getMain().getTemp() - 273.15); // Convert to Celsius
            data.setFeelsLike(response.getMain().getFeels_like() - 273.15);
            data.setMain(response.getWeather().get(0).getMain());
            data.setHumidity(response.getMain().getHumidity());
            data.setWindSpeed(response.getWind().getSpeed());
            data.setTimestamp(LocalDateTime.now());

            weatherDataRepository.save(data);

            updateDailySummary(city);
        }
    }

    // Method to get the latest weather data for a city
    public WeatherData getLatestWeatherData(String city) {
        return weatherDataRepository.findTopByCityOrderByTimestampDesc(city);
    }

    // Method to get daily summaries for a city
    public List<DailyWeatherSummary> getDailySummaries(String city) {
        return summaryRepository.findByCity(city); // This should return a list
    }

    private void updateDailySummary(String city) {
        // Fetch the latest 6 WeatherData entries for the given city
        List<WeatherData> todayData = weatherDataRepository.findTop6ByCityOrderByTimestampDesc(city);

        // Initialize temperature statistics
        double avgTemp = 0.0;
        double minTemp = Double.MAX_VALUE;
        double maxTemp = Double.MIN_VALUE;

        // Handle empty data
        if (!todayData.isEmpty()) {
            avgTemp = todayData.stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0);
            minTemp = todayData.stream().mapToDouble(WeatherData::getTemperature).min().orElse(0.0);
            maxTemp = todayData.stream().mapToDouble(WeatherData::getTemperature).max().orElse(0.0);
        }

        // Determine the dominant weather
        String dominantWeather = todayData.stream()
                .map(WeatherData::getMain)
                .reduce((a, b) -> a.equals(b) ? a : "Mixed").orElse("Clear");

        // Fetch existing summary for today
        DailyWeatherSummary existingSummary = summaryRepository.findByCityAndDate(city, LocalDate.now());

        DailyWeatherSummary summary;
        if (existingSummary != null) {
            // Update existing summary if it already exists
            existingSummary.setAvgTemperature(avgTemp);
            existingSummary.setMinTemperature(todayData.isEmpty() ? 0.0 : minTemp); // Handle case where there is no data
            existingSummary.setMaxTemperature(todayData.isEmpty() ? 0.0 : maxTemp); // Handle case where there is no data
            existingSummary.setDominantWeather(dominantWeather);
            summary = existingSummary;
        } else {
            // Create a new summary if it doesn't exist
            summary = new DailyWeatherSummary();
            summary.setCity(city);
            summary.setDate(LocalDate.now());
            summary.setAvgTemperature(avgTemp);
            summary.setMinTemperature(todayData.isEmpty() ? 0.0 : minTemp); // Handle case where there is no data
            summary.setMaxTemperature(todayData.isEmpty() ? 0.0 : maxTemp); // Handle case where there is no data
            summary.setDominantWeather(dominantWeather);
        }

        // Save the summary (either updated or new)
        summaryRepository.save(summary);
    }


    @Scheduled(fixedRate = 300000)
    public void scheduledWeatherFetch() {
        // List of cities to fetch
        List<String> cities = List.of("Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad");
        cities.forEach(this::fetchWeatherData);
    }
}
