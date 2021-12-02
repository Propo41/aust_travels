package com.pixieium.austtravels.models;


public class UserSettings {
    private Boolean isPingNotification = true;
    private Boolean isLocationNotification = false;
    private String primaryBus = "None";

    public UserSettings() {
    }

    public UserSettings(Boolean isPingNotification, Boolean isLocationNotification, String primaryBus) {
        this.isPingNotification = isPingNotification;
        this.isLocationNotification = isLocationNotification;
        this.primaryBus = primaryBus;
    }

    public Boolean getPingNotification() {
        return isPingNotification;
    }

    public void setPingNotification(Boolean pingNotification) {
        isPingNotification = pingNotification;
    }

    public Boolean getLocationNotification() {
        return isLocationNotification;
    }

    public void setLocationNotification(Boolean locationNotification) {
        isLocationNotification = locationNotification;
    }

    public String getPrimaryBus() {
        return primaryBus;
    }

    public void setPrimaryBus(String primaryBus) {
        this.primaryBus = primaryBus;
    }
}
