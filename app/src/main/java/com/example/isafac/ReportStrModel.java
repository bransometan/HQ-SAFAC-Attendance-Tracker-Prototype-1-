package com.example.isafac;

public class ReportStrModel {

    // All declared variables name must be the SAME as the one u declared in firestore if not it wil not show!

    private String Date;
    private String Branch;
    private String AM_PM_Status;
    private String ReportedStrength;
    private String ReportedByUser;

    public ReportStrModel() {
    }

    public ReportStrModel(String date, String branch, String amPmStatus, String reportStrStatus, String reportedBy) {
        this.Date = date;
        this.Branch = branch;
        this.AM_PM_Status = amPmStatus;
        this.ReportedStrength = reportStrStatus;
        this.ReportedByUser = reportedBy;
    }

    public String getDate() {
        return Date;
    }

    public String getBranch() {
        return Branch;
    }

    public String getAM_PM_Status() {
        return AM_PM_Status;
    }

    public String getReportedStrength() {
        return ReportedStrength;
    }

    public String getReportedByUser() {
        return ReportedByUser;
    }
}
