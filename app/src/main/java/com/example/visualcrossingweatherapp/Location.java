package com.example.visualcrossingweatherapp;
import java.io.Serializable;

public class Location implements Serializable {
        protected double latitude;
        protected double longitude;
        protected String resolvedAddress;

        Location(double latitude, double longitude, String resolvedAddress) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.resolvedAddress = resolvedAddress;
        }
}
