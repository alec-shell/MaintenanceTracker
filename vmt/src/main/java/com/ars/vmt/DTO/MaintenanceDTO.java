/**
 * 11/23/2026   Class created
 */

package com.ars.vmt.DTO;

import java.math.BigDecimal;

public class MaintenanceDTO {
    private int maintId;
    private int vehId;
    private int mileage;
    private String date;
    private String location;
    private String type;
    private BigDecimal cost;
    private String notes;

    public MaintenanceDTO(int maintId, int vehId, int mileage, String date,
                          String location, String type, BigDecimal cost, String notes) {
        this.maintId = maintId;
        this.vehId = vehId;
        this.mileage = mileage;
        this.date = date;
        this.location = location;
        this.type = type;
        this.cost = cost;
        this.notes = notes;
    }

    public MaintenanceDTO() {
        maintId = -1;
        vehId = -1;
        mileage = -1;
        date = null;
        location = null;
        type = null;
        cost = null;
        notes = null;
    }

    public void setMaintId(int maintId) {
        this.maintId = maintId;
    }

    public void setVehId(int vehId) {
        this.vehId = vehId;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getMaintId() {
        return maintId;
    }

    public int getVehId() {
        return vehId;
    }

    public int getMileage() {
        return mileage;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getNotes() {
        return notes;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Maintenance ID: "); sb.append(this.maintId);
        sb.append("\nVehicle ID: "); sb.append(this.vehId);
        sb.append("\nMileage: "); sb.append(this.mileage);
        sb.append("\nDate: "); sb.append(this.date);
        sb.append("\nLocation: "); sb.append(this.location);
        sb.append("\nType: "); sb.append(this.type);
        sb.append("\nCost: "); sb.append(this.cost);
        sb.append("\nNotes: "); sb.append(this.notes);
        
        return sb.toString();
    }
}
