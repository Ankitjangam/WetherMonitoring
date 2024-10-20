package com.weathermonitoring.Repository;


import com.weathermonitoring.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    // Custom query method to find the latest weather data for a specific city
    List<WeatherData> findTop6ByCityOrderByTimestampDesc(String city);

    // Method to find the latest weather data (single record) for a specific city
    WeatherData findTopByCityOrderByTimestampDesc(String city);
}

