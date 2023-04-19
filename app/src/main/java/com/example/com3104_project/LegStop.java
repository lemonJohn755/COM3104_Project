package com.example.com3104_project;

public class LegStop {
    String name;
    double codLat;
    double codLon;

    public LegStop(String name, double codLat, double codLon) {
        this.name = name;
        this.codLat = codLat;   // Coordinate latitude
        this.codLon = codLon;   // Coordinate longitude
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCodLat() {
        return codLat;
    }

    public void setCodLat(double codLat) {
        this.codLat = codLat;
    }

    public double getCodLon() {
        return codLon;
    }

    public void setCodLon(double codLon) {
        this.codLon = codLon;
    }
}
