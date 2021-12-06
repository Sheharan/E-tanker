package com.example.etanker_supplier.Model;

public class PreviousOrder {
    private String address;
    private String deliveredDate;
    private String email;
    private String feedback;
    private String latitude;
    private String longitude;
    private String name;
    private String orderedDate;
    private String phone;
    private String rating;
    private String supplierID;
    private String supplierCompanyName;
    private String tankerNumber;
    private String verificationNumber;

    public PreviousOrder() {
    }

    public PreviousOrder(String address, String deliveredDate, String email, String feedback, String latitude, String longitude, String name, String orderedDate, String phone, String rating, String supplierID, String supplierCompanyName, String tankerNumber, String verificationNumber) {
        this.address = address;
        this.deliveredDate = deliveredDate;
        this.email = email;
        this.feedback = feedback;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.orderedDate = orderedDate;
        this.phone = phone;
        this.rating = rating;
        this.supplierID = supplierID;
        this.supplierCompanyName = supplierCompanyName;
        this.tankerNumber = tankerNumber;
        this.verificationNumber = verificationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public String getTankerNumber() {
        return tankerNumber;
    }

    public void setTankerNumber(String tankerNumber) {
        this.tankerNumber = tankerNumber;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }
}

