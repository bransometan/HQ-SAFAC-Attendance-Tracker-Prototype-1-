package com.example.isafac;

public class OverallStrModel {

    // All declared variables name must be the SAME as the one u declared in firestore if not it wil not show!

    private String Date;
    private String AM_PM_Status;
    private String OverallStrength;
    private String ReportedByUser;

    public OverallStrModel() {
    }

    public OverallStrModel(String date, String amPmStatus, String overallStrengthStatus, String reportedBy) {
        this.Date = date;
        this.AM_PM_Status = amPmStatus;
        this.OverallStrength = overallStrengthStatus;
        this.ReportedByUser = reportedBy;
    }

    public String getDate() {
        return Date;
    }

    public String getAM_PM_Status() {
        return AM_PM_Status;
    }

    public String getOverallStrength() {
        return OverallStrength;
    }

    public String getReportedByUser() {
        return ReportedByUser;
    }
}
