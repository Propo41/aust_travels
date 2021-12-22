package com.pixieium.austtravels.models;

public class AppUpdate {
    private String versionCode;
    private boolean isAvailable;

    public AppUpdate() {
    }

    public AppUpdate(String versionCode, boolean isAvailable) {
        this.versionCode = versionCode;
        this.isAvailable = isAvailable;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
