/**
 * 11/23/2026   Class created
 */

package com.ars.vmt.DTO;

public class VehicleDTO {
    private int vehId;
    private int year;
    private String vin;
    private String make;
    private String model;
    private String color;
    private String purchaseDate;
    private String soldDate;

    public VehicleDTO(int vehId, int year, String vin, String make, String model,
                      String color, String purchaseDate, String soldDate) {
        this.vehId = vehId;
        this.year = year;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.color = color;
        this.purchaseDate = purchaseDate;
        this.soldDate = soldDate;
    }

    public VehicleDTO() {
        vehId = -1;
        year = -1;
        vin = null;
        make = null;
        model = null;
        color = null;
        purchaseDate = null;
        soldDate = null;
    }

    public void setVehId(int vehId) {
        this.vehId = vehId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setSoldDate(String soldDate) {
        this.soldDate = soldDate;
    }

    public int getVehId() {
        return vehId;
    }
    public int getYear() {
        return year;
    }

    public String getVin() {
        return vin;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getSoldDate() {
        return soldDate;
    }

}
