package com.sasha.androidtracker.model;

/**
 * Project : AdroidTracker
 *
 * @author Sasha Antipin
 * @version 0.1
 * @autor Sasha
 * @since 29-11-2015
 */

public class GPSData {

    private String timeStamp;
    private String latitude;
    private String longitude;

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return timeStamp + '\n' +
                ", latitude= " + latitude + ' ' +
                ", longitude= " + longitude;
    }
}
