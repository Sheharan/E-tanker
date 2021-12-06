package com.example.etanker_supplier.Model;

public class OrderInfo {
    private String address;
    private String email;
    private String latitude;
    private String longitude;
    private String name;
    private String orderedDate;
    private String paymentToken;
    private String phone;
    private String supplierID;
    private String supplierCompanyName;
    private String tankerNumber;
    private String verificationNumber;

    public OrderInfo() {
    }

    public OrderInfo(String address, String email, String latitude, String longitude, String name, String orderedDate, String paymentToken, String phone, String supplierID, String supplierCompanyName, String tankerNumber, String verificationNumber) {
        this.address = address;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.orderedDate = orderedDate;
        this.paymentToken = paymentToken;
        this.phone = phone;
        this.supplierID = supplierID;
        this.supplierCompanyName = supplierCompanyName;
        this.tankerNumber = tankerNumber;
        this.verificationNumber = verificationNumber;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
