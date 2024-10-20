package com.weathermonitoring.model;

import java.util.List;

public class WeatherResponse {

    private Main main;
    private Wind wind;
    private List<Weather> weather;

    // Getters and setters

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    // Nested class for Main
    public static class Main {
        private double temp; // Current temperature in Kelvin
        private double feels_like; // Perceived temperature in Kelvin
        private int humidity;

        // Getters and setters
        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }

    // Nested class for Wind
    public static class Wind {
        private double speed;

        // Getters and setters
        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }

    // Nested class for Weather
    public static class Weather {
        private String main; // Weather condition (e.g., Clear, Rain)
        private String description; // Weather description

        // Getters and setters
        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
