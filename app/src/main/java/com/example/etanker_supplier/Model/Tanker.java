package com.example.etanker_supplier.Model;

public class Tanker {
    private String tankerNumber;
    private String price;
    private String source;
    private String stickerColor;
    private String literCapacity;
    private String driverName;
    private String driverContact;
    private String tankerStatus;
    private String ownerEmail;

    public Tanker() {
    }

    public Tanker(String tankerNumber, String price, String source, String stickerColor, String literCapacity, String driverName, String driverContact, String tankerStatus,String ownerEmail) {
        this.tankerNumber = tankerNumber;
        this.price = price;
        this.source = source;
        this.stickerColor = stickerColor;
        this.literCapacity = literCapacity;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.tankerStatus = tankerStatus;
        this.ownerEmail=ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getTankerNumber() {
        return tankerNumber;
    }

    public void setTankerNumber(String tankerNumber) {
        this.tankerNumber = tankerNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStickerColor() {
        return stickerColor;
    }

    public void setStickerColor(String stickerColor) {
        this.stickerColor = stickerColor;
    }

    public String getLiterCapacity() {
        return literCapacity;
    }

    public void setLiterCapacity(String literCapacity) {
        this.literCapacity = literCapacity;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getTankerStatus() {
        return tankerStatus;
    }

    public void setTankerStatus(String tankerStatus) {
        this.tankerStatus = tankerStatus;
    }
}
