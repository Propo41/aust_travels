package com.pixieium.austtravels.models;

import com.google.firebase.database.Exclude;

public class UserInfo {
    private String email;
    private String name;
    private String semester;
    private String department;
    private String universityId;
    private String userImage;
    @Exclude
    private String password;

    public UserInfo() {
    }

    public UserInfo(String email, String name, String semester, String department, String universityId, String userImage) {
        this.email = email;
        this.name = name;
        this.semester = semester;
        this.department = department;
        this.universityId = universityId;
        this.userImage = userImage;
    }

    public UserInfo(String email, String password, String name, String semester, String department, String universityId, String userImage) {
        this.email = email;
        this.name = name;
        this.semester = semester;
        this.department = department;
        this.universityId = universityId;
        this.userImage = userImage;
        this.password = password;
    }

    public UserInfo(String email, String userImage, String universityId) {
        this.email = email;
        this.userImage = userImage;
        this.universityId = universityId;

    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    private Boolean isValidEmail(CharSequence target) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return !target.toString().isEmpty() && email.matches(emailPattern);
    }

    public String validateInput() {
        String errorMessage = null;
        String fieldRequired = "Field is required";

        if (!isValidEmail(this.email)) {
            errorMessage = "Please enter a valid email";
        } else {
            if (!email.split("@")[1].equals("aust.edu")) {
                errorMessage = "You must enter your institutional mail";
            }
        }

        if (email.isEmpty()) {
            errorMessage = "You must enter your institutional mail";
        }

        if (universityId.isEmpty()) {
            errorMessage = fieldRequired;
        }

        if (password.isEmpty()) {
            errorMessage = fieldRequired;

        }
        if (name.isEmpty()) {
            errorMessage = fieldRequired;

        }
        if (name.length() > 20) {
            errorMessage = "Please enter a name of 20 characters";

        }
        if (semester.isEmpty()) {
            errorMessage = fieldRequired;

        }
        if (semester.isEmpty()) {
            errorMessage = fieldRequired;
        }

        return errorMessage;
    }

}
