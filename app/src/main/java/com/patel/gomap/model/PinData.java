package com.patel.gomap.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "pinDataTable")
public class PinData {

    @PrimaryKey
    @SerializedName("id")
    int id = 0;

    @SerializedName("name")
    String name = null;

    @SerializedName("latitude")
    double latitude = 0.0;

    @SerializedName("longitude")
    double longitude = 0.0;

    @SerializedName("description")
    String despcription = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDespcription() {
        return despcription;
    }

    public void setDespcription(String despcription) {
        this.despcription = despcription;
    }

    public PinData(int id, String name, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PinData(PinData pinData){
        this.id = pinData.id;
        this.name = pinData.name;
        this.longitude = pinData.longitude;
        this.latitude = pinData.latitude;
    }
    public PinData(){}
}
