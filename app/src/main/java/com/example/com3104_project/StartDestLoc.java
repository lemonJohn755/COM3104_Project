package com.example.com3104_project;

import java.io.Serializable;

public class StartDestLoc implements Serializable {
    double fromLat, fromLon, toLat, toLon;
    String fromAddr, toAddr;

    public StartDestLoc(double fromLat, double fromLog, double toLat, double toLog, String fromAddr, String toAddr) {
        this.fromLat = fromLat;
        this.fromLon = fromLog;
        this.toLat = toLat;
        this.toLon = toLog;
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
    }

    public double getFromLat() {
        return fromLat;
    }

    public void setFromLat(double fromLat) {
        this.fromLat = fromLat;
    }

    public double getFromLon() {
        return fromLon;
    }

    public void setFromLon(double fromLog) {
        this.fromLon = fromLog;
    }

    public double getToLat() {
        return toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }

    public double getToLon() {
        return toLon;
    }

    public void setToLon(double toLog) {
        this.toLon = toLon;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }
}
