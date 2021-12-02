package com.pixieium.austtravels.models;

import com.google.firebase.database.Exclude;

public class Volunteer extends UserInfo {
    private Long totalContribution;
    private boolean status;
    private String contact;
    @Exclude
    private String totalContributionFormatted;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Volunteer() {
    }

    public Volunteer(UserInfo userInfo, String totalContributionFormatted) {
        super(userInfo.getEmail(), userInfo.getName(), userInfo.getSemester(),
                userInfo.getDepartment(), userInfo.getUniversityId(), userInfo.getUserImage());
        this.totalContributionFormatted = totalContributionFormatted;

    }

    public Volunteer(UserInfo userInfo, Long totalContribution, String contact) {
        super(userInfo.getEmail(), userInfo.getName(), userInfo.getSemester(),
                userInfo.getDepartment(), userInfo.getUniversityId(), userInfo.getUserImage());
        this.totalContribution = totalContribution;
        this.contact = contact;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTotalContributionFormatted() {
        return totalContributionFormatted;
    }

    public void setTotalContributionFormatted(String totalContributionFormatted) {
        this.totalContributionFormatted = totalContributionFormatted;
    }

    public Long getTotalContribution() {
        return totalContribution;
    }

    public void setTotalContribution(Long totalContribution) {
        this.totalContribution = totalContribution;
    }


}
