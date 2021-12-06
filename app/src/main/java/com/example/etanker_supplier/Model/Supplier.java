package com.example.etanker_supplier.Model;

public class Supplier {
    private String company_name;
    private String name;
    private String email;
    private String phone_number;
    private String registrationNumber;
    private String location;
    private String latitude;
    private String longitude;
    private String ID;


    public Supplier() {
    }

    public Supplier(String company_name, String name, String email, String phone_number, String registrationNumber, String location, String latitude, String longitude, String ID) {
        this.company_name = company_name;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.registrationNumber = registrationNumber;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
