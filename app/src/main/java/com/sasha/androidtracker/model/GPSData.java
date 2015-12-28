package com.sasha.androidtracker.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.sasha.androidtracker.MainActivity;

/**
 * GPSData class
 * to instantiate GPSData objects
 * Project : AdroidTracker
 *
 * @author Sasha Antipin
 * @version 0.9
 * @autor Sasha
 * @since 29-11-2015
 */

public class GPSData implements Parcelable{

    private long id;
    private String timeStamp;
    private double latitude;
    private double longitude;
    private float accelerometerX;
    private float accelerometerY;
    private float accelerometerZ;

    public GPSData(){}

    public GPSData(Parcel in) {
        Log.i(MainActivity.LOGTAG, "Parcel constructor");

        id = in.readLong();
        timeStamp = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        accelerometerX = in.readFloat();
        accelerometerY = in.readFloat();
        accelerometerZ = in.readFloat();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public float getAccelerometerX() {
        return accelerometerX;
    }

    public void setAccelerometerX(float accelerometerX) {
        this.accelerometerX = accelerometerX;
    }

    public float getAccelerometerY() {
        return accelerometerY;
    }

    public void setAccelerometerY(float accelerometerY) {
        this.accelerometerY = accelerometerY;
    }

    public float getAccelerometerZ() {
        return accelerometerZ;
    }

    public void setAccelerometerZ(float accelerometerZ) {
        this.accelerometerZ = accelerometerZ;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * method to generate close to unique hash codes
     *
     * @return hashCode int value
     */
    @Override
    public int hashCode() {
        int value = 0;
        for (char i : timeStamp.toCharArray()) value += i;

        return ((7 * value)
                ^ (11 * (int) latitude)
                ^ (53 * (int) longitude));
    }

    @Override
    public String toString() {
        return timeStamp + '\n' +
                ", latitude= " + latitude + ' ' +
                ", longitude= " + longitude;
    }



    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * will be called automatically whenever Android needs to get a flattened version of this object
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.i(MainActivity.LOGTAG, "writeToParcel");

        dest.writeLong(id);
        dest.writeString(timeStamp);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(accelerometerX);
        dest.writeFloat(accelerometerY);
        dest.writeFloat(accelerometerZ);
    }

    public static final Parcelable.Creator<GPSData> CREATOR =
            new Parcelable.Creator<GPSData>() {

                @Override
                public GPSData createFromParcel(Parcel source) {
                    Log.i(MainActivity.LOGTAG, "createFromParcel");
                    return new GPSData(source);
                }

                @Override
                public GPSData[] newArray(int size) {
                    Log.i(MainActivity.LOGTAG, "newArray");
                    return new GPSData[size];
                }

            };
}
