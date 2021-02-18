package com.example.tracknowdemo.ui.map;

public class MyLocation {
    private String sharePass,latitude,longitude;

    public MyLocation() {
    }

    public MyLocation(String latitude, String longitude, String sharePass) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sharePass = sharePass;
    }

    public String getSharePass() {
        return sharePass;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setSharePass(String sharePass) {
        this.sharePass = sharePass;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
