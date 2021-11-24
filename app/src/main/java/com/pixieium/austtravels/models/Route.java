package com.pixieium.austtravels.models;

public class Route {
    private String place;
    private String estTime;
    private String mapPlaceName;
    private String mapPlaceId;
    private String latitude;
    private String longitude;

    public Route() {
    }

    public Route(String place, String estTime, String mapPlaceName, String mapPlaceId, String latitude, String longitude) {
        this.place = place;
        this.estTime = estTime;
        this.mapPlaceName = mapPlaceName;
        this.mapPlaceId = mapPlaceId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public String getMapPlaceName() {
        return mapPlaceName;
    }

    public void setMapPlaceName(String mapPlaceName) {
        this.mapPlaceName = mapPlaceName;
    }

    public String getMapPlaceId() {
        return mapPlaceId;
    }

    public void setMapPlaceId(String mapPlaceId) {
        this.mapPlaceId = mapPlaceId;
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
}