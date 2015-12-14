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
    private String accelerometerX;
    private String accelerometerY;
    private String accelerometerZ;

    public String getAccelerometerX() { return accelerometerX; }
    public void setAccelerometerX(String accelerometerX) {
        this.accelerometerX = accelerometerX;
    }

    public String getAccelerometerY() {
        return accelerometerY;
    }
    public void setAccelerometerY(String accelerometerY) {
        this.accelerometerY = accelerometerY;
    }

    public String getAccelerometerZ() {
        return accelerometerZ;
    }
    public void setAccelerometerZ(String accelerometerZ) {
        this.accelerometerZ = accelerometerZ;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {  this.timeStamp = timeStamp; }

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

    /**
     * method to generate close to unique hash codes
     * @return hashCode int value
     */
    @Override
    public int hashCode() {
        int value = 0;
        for (char i : timeStamp.toCharArray()) value += i;
        float lat = Float.parseFloat(latitude);
        float lon = Float.parseFloat(longitude);

        return (    (7 * value)
                    ^ (11 * (int) lat)
                    ^ (53 * (int) lon) );
    }

    @Override
    public String toString() {
        return timeStamp + '\n' +
                ", latitude= " + latitude + ' ' +
                ", longitude= " + longitude;
    }
}
