package com.example.kyle.cnapaycalculator;

public class DayTracker {

    private String date;
    private int regMHours, regEHours, regOHours, OTMHours, OTEHours, OTOHours;

    public DayTracker(String date, int regMHours, int regEHours, int regOHours, int OTMHours, int OTEHours, int OTOHours) {
        this.date = date;
        this.regMHours = regMHours;
        this.regEHours = regEHours;
        this.regOHours = regOHours;
        this.OTMHours = OTMHours;
        this.OTEHours = OTEHours;
        this.OTOHours = OTOHours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRegMHours() {
        return regMHours;
    }

    public void setRegMHours(int regMHours) {
        this.regMHours = regMHours;
    }

    public int getRegEHours() {
        return regEHours;
    }

    public void setRegEHours(int regEHours) {
        this.regEHours = regEHours;
    }

    public int getRegOHours() {
        return regOHours;
    }

    public void setRegOHours(int regOHours) {
        this.regOHours = regOHours;
    }

    public int getOTMHours() {
        return OTMHours;
    }

    public void setOTMHours(int OTMHours) {
        this.OTMHours = OTMHours;
    }

    public int getOTEHours() {
        return OTEHours;
    }

    public void setOTEHours(int OTEHours) {
        this.OTEHours = OTEHours;
    }

    public int getOTOHours() {
        return OTOHours;
    }

    public void setOTOHours(int OTOHours) {
        this.OTOHours = OTOHours;
    }
}
