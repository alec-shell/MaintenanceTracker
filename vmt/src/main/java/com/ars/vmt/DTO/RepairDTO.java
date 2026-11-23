/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.DTO;

public class RepairDTO {
    private int repairId;
    private int vehId;
    private int mileage;
    private String eventTitle;
    private String date;
    private String location;
    private double cost;
    private String notes;

    public RepairDTO(int repairId, int vehId, int mileage,  String eventTitle, String date,
                     String location, double cost, String notes) {
        this.repairId = repairId;
        this.vehId = vehId;
        this.mileage = mileage;
        this.eventTitle = eventTitle;
        this.date = date;
        this.location = location;
        this.cost = cost;
        this.notes = notes;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    public void setVehId(int vehId) {
        this.vehId = vehId;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRepairId() {
        return repairId;
    }

    public int getVehId() {
        return vehId;
    }

    public int getMileage() {
        return mileage;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public double getCost() {
        return cost;
    }

    public String getNotes() {
        return notes;
    }

}
