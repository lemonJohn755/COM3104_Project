package com.example.com3104_project;

public class BusStop {
    String route;

    String bound;
    String service_type;
    int seq;
    String stopID;
    String stopName;
    double lat;
    double lon;

    public BusStop(String route, String bound, int seq, String stopID, String stopName, double lat, double lon) {
        this.route = route;
        this.bound = bound;
        this.service_type = service_type;
        this.seq = seq;
        this.stopID = stopID;
        this.stopName = stopName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }



}
