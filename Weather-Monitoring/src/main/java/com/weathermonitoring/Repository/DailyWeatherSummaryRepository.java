package com.weathermonitoring.Repository;



import com.weathermonitoring.model.DailyWeatherSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyWeatherSummaryRepository extends JpaRepository<DailyWeatherSummary, Long> {

    // Custom query method to find daily weather summaries by city and date
    DailyWeatherSummary findByCityAndDate(String city, LocalDate date);

    // Method to find all summaries for a specific city
    List<DailyWeatherSummary> findByCity(String city);
}
