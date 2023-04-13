package com.example.com3104_project;

import java.io.Serializable;

public class Bus implements Serializable {

    String route;
    String company;
    String fromLoc;
    String toLoc;

    public Bus(String route, String fromLoc, String toLoc, String company) {
        this.route = route;
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
        this.company = company;
    }


    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(String fromLoc) {
        this.fromLoc = fromLoc;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
