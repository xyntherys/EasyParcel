package com.easyparcel.model;


public class Parcel {

    private int parcelId;
    private String type;
    private String address;
    private String phone;
    private String email;
    private String bookedAt;
    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getParcelId() {
        return parcelId;
    }

    public void setParcelId(int parcelId) {
        this.parcelId = parcelId;
    }

    public void setParcelId(String s) {
        this.parcelId = Integer.valueOf(s);
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(String bookedAt) {
        this.bookedAt = bookedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
