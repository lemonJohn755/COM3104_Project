package com.example.com3104_project;

public class Leg {
    String travel_mode;     // e.g. walk/ transit
    String duration_seconds;
    String vehicle_types;   // e.g. bus/ metro
    String brand;       // e.g. KMB/ 港鐵 MTR
    String name;        // e.g. 277E/ 屯馬綫 Tuen Ma Line
    String stopsJsonStr;    // display in child item in RecyclerView

    public Leg(String travel_mode, String duration_seconds, String vehicle_types, String brand, String name, String stopsJsonStr) {
        this.travel_mode = travel_mode;
        this.duration_seconds = duration_seconds;
        this.vehicle_types = vehicle_types;
        this.brand = brand;
        this.name = name;
        this.stopsJsonStr = stopsJsonStr;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getDuration_seconds() {
        return duration_seconds;
    }

    public void setDuration_seconds(String duration_seconds) {
        this.duration_seconds = duration_seconds;
    }

    public String getVehicle_types() {
        return vehicle_types;
    }

    public void setVehicle_types(String vehicle_types) {
        this.vehicle_types = vehicle_types;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStopsJsonStr() {
        return stopsJsonStr;
    }

    public void setStopsJsonStr(String stopsJsonStr) {
        this.stopsJsonStr = stopsJsonStr;
    }
}
