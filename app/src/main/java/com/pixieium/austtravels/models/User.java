package com.pixieium.austtravels.models;

public class User {
    private String userId;
    private String name;
    private String semester;
    private String department;
    private String email;

    public User(String userId, String name, String semester, String department, String email) {
        this.userId = userId;
        this.name = name;
        this.semester = semester;
        this.department = department;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
