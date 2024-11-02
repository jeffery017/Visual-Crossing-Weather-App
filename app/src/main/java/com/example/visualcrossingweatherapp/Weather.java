package com.example.visualcrossingweatherapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Weather implements Serializable {
    private final long datetimeEpoch;
    private final double tempmax;
    private final double tempmin;
    private final double precipprob;
    private final double humidity;

    private final double windgust;
    private final double windspeed;
    private final double winddir;

    private final double visibility;
    private final double cloudcover;
    private final double uvindex;

    private final String conditions;
    private final String icon;
    private final String description;
    private final long sunriseEpoch;
    private final long sunsetEpoch;

    private ArrayList<HourlyWeather> hours;

    public Weather(int datetimeEpoch, double tempmax, double tempmin, double precipprob, double humidity, double windgust, double windspeed, double winddir, double visibility, double cloudcover, double uvindex, String conditions, String icon, String description, long sunriseEpoch, long sunsetEpoch, ArrayList<HourlyWeather> hours) {
        this.datetimeEpoch = datetimeEpoch;
        this.tempmax = tempmax;
        this.tempmin = tempmin;
        this.precipprob = precipprob;
        this.humidity = humidity;
        this.windgust = windgust;
        this.windspeed = windspeed;
        this.winddir = winddir;
        this.visibility = visibility;
        this.cloudcover = cloudcover;
        this.uvindex = uvindex;
        this.conditions = conditions;
        this.icon = icon;
        this.description = description;
        this.sunriseEpoch = sunriseEpoch;
        this.sunsetEpoch = sunsetEpoch;
        this.hours = hours;
    }

    public long getDatetimeEpoch() {return datetimeEpoch;}
    public double getTempmaxF() {return tempmax;}
    public double getTempmaxC() {return (tempmax - 32) * 5 / 9;}
    public double getTempminF() {return tempmin;}
    public double getTempminC() {return (tempmin - 32) * 5 / 9;}
    public double getPrecipprob() {return precipprob;}
    public double getHumidity() {return humidity;}
    public double getWindgust() {return windgust;}
    public double getWindspeed() {return windspeed;}
    public double getWinddir() {return winddir;}
    public double getVisibility() {return visibility;}
    public double getCloudcover() {return cloudcover;}
    public double getUvindex() {return uvindex;}
    public long getSunriseEpoch() {return sunriseEpoch;}
    public long getSunsetEpoch() {return sunsetEpoch;}
    public String getConditions() {return conditions;}
    public String getIcon() {return icon;}
    public String getDescription() {return description;}
    public ArrayList<HourlyWeather> getHours() {return hours;}

    // Hourly Weather class
    public static class HourlyWeather implements Serializable{
        private final long datetimeEpoch;
        private final double temp;
        private final double feelslike;
        private final double humidity;
        private final double windgust;
        private final double windspeed;
        private final double winddir;
        private final double uvindex;
        private final String datetime;
        private final String conditions;
        private final String icon;

        // constructors
        public HourlyWeather(String datetime, long datetimeEpoch, double temp, double feelslike, double humidity, double windgust, double windspeed, double winddir, double uvindex, String conditions, String icon) {
            this.datetime = datetime;
            this.datetimeEpoch = datetimeEpoch;
            this.temp = temp;
            this.feelslike = feelslike;
            this.humidity = humidity;
            this.windgust = windgust;
            this.windspeed = windspeed;
            this.winddir = winddir;
            this.uvindex = uvindex;
            this.conditions = conditions;
            this.icon = icon;
        }
        // getters
        public String getDateTime() {
            return datetime;
        }
        public long getDateTimeEpoch() {
            return datetimeEpoch;
        }
        public double getTempF() {
            return temp;
        }
        public double getTempC() {
            return (temp - 32) * 5 / 9;
        }

        public double getFeelslikeF() {
            return feelslike;
        }
        public double getFeelslikeC() {
            return (feelslike - 32) * 5 / 9;
        }
        public double getHumidity() {
            return humidity;
        }
        public double getWindgust() {
            return windgust;
        }
        public double getWindspeed() {
            return windspeed;
        }
        public double getWinddir() {
            return winddir;
        }
        public double getUvindex() {
            return uvindex;
        }
        public String getConditions() {
            return conditions;
        }
        public String getIcon() {
            return icon;
        }
    }
}


