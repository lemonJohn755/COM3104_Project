package com.example.com3104_project;

import java.io.Serializable;

public class Suggest implements Serializable {
    String route;
    int durationSec;
    String routeJson;

    public Suggest(String route, int durationSec, String routeJson) {
        this.route = route;
        this.durationSec = durationSec;
        this.routeJson = routeJson;
    }


    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(int durationSec) {
        this.durationSec = durationSec;
    }

    public String getRouteJson() {
        return routeJson;
    }

    public void setRouteJson(String routeJson) {
        this.routeJson = routeJson;
    }
}
