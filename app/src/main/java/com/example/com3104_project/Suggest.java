package com.example.com3104_project;

import com.google.gson.JsonObject;

import java.io.Serializable;

public class Suggest implements Serializable {
    String route;
    int durationSec;
    JsonObject routeObject;

    public Suggest(String route, int durationSec, JsonObject routeObject) {
        this.route = route;
        this.durationSec = durationSec;
        this.routeObject = routeObject;
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

    public JsonObject getRouteObject() {
        return routeObject;
    }

    public void setRouteObject(JsonObject routeObject) {
        this.routeObject = routeObject;
    }
}
