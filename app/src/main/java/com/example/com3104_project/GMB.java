package com.example.com3104_project;

public class GMB {
    String route_id;
    String region;
    String route_code;

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRoute_code() {
        return route_code;
    }

    public void setRoute_code(String route_code) {
        this.route_code = route_code;
    }

    public GMB(String route_id, String region, String route_code) {
        this.route_id = route_id;
        this.region = region;
        this.route_code = route_code;
    }
}
