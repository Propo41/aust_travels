package com.pixieium.austtravels.models;

public class Route {
    private String place;
    private String estTime;
    private String mapPlaceName;
    private String mapPlaceId;

    public Route(String place, String estTime, String mapPlaceName, String mapPlaceId) {
        this.place = place;
        this.estTime = estTime;
        this.mapPlaceName = mapPlaceName;
        this.mapPlaceId = mapPlaceId;
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
}
