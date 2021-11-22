package com.pixieium.austtravels.models;

import com.squareup.picasso.Picasso;

public class Volunteer {
    String name;
    String roll;
    String imageUrl;


    public Volunteer() {
    }

    public Volunteer(String name, String roll) {
        this.name = name;
        this.roll = roll;
    }
    public Volunteer(String name, String roll, String imageUrl) {
        this.name = name;
        this.roll = roll;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
