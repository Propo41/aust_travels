package com.pixieium.austtravels.auth;

public class UserInfo {
    String email,name,semester,department;

    public UserInfo(String email, String name, String semester, String department) {
        this.email = email;
        this.name = name;
        this.semester = semester;
        this.department = department;
    }

    public UserInfo() {
    }
}
