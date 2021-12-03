package com.pixieium.austtravels.models;


public class UserSettings {
    private boolean isPingNotification;
    private boolean isLocationNotification;
    private String primaryBus;

    public UserSettings() {
    }

    public UserSettings(boolean isPingNotification, boolean isLocationNotification, String primaryBus) {
        this.isPingNotification = isPingNotification;
        this.isLocationNotification = isLocationNotification;
        this.primaryBus = primaryBus;
    }

    public boolean getPingNotification() {
        return isPingNotification;
    }

    public void setPingNotification(boolean pingNotification) {
        isPingNotification = pingNotification;
    }

    public boolean getLocationNotification() {
        return isLocationNotification;
    }

    public void setLocationNotification(boolean locationNotification) {
        isLocationNotification = locationNotification;
    }

    public String getPrimaryBus() {
        return primaryBus;
    }

    public void setPrimaryBus(String primaryBus) {
        this.primaryBus = primaryBus;
    }
}
