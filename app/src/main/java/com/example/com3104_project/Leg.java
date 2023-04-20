package com.example.com3104_project;

import java.util.List;

public class Leg {
    String travel_mode;     // e.g. walk/ transit
    int duration_seconds;
    String vehicle_types;   // e.g. bus/ metro
    String brand;       // e.g. KMB/ 港鐵 MTR
    String name;        // e.g. 277E/ 屯馬綫 Tuen Ma Line
    List<LegStop> legStopList;    // display in child item in nested RecyclerView

    public Leg(String travel_mode, int duration_seconds, String vehicle_types, String brand,
               String name, List<LegStop> legStopList) {
        this.travel_mode = travel_mode;
        this.duration_seconds = duration_seconds;
        this.vehicle_types = vehicle_types;
        this.brand = brand;
        this.name = name;
        this.legStopList = legStopList;
    }

    public List<LegStop> getLegStopList() {
        return legStopList;
    }

    public void setLegStopList(List<LegStop> legStopList) {
        this.legStopList = legStopList;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public int getDuration_seconds() {
        return duration_seconds;
    }

    public void setDuration_seconds(int duration_seconds) {
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

}
