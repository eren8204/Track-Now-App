package com.example.tracknowdemo.ui.map;

public class MyLocation {
    private String sharePass;
    private Double latitude, longitude;

    public MyLocation(double latitude, double longitude, String sharePass) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sharePass = sharePass;
    }

    public MyLocation(String latitude, String longitude, String sharePass) {
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.sharePass = sharePass;
    }
    public MyLocation(){
        
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setSharePass(String sharePass) {
        this.sharePass = sharePass;
    }
    public String getSharePass() {
        return sharePass;
    }
    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
