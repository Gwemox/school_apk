package com.ynov.tbu.schoolexplorer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by titic on 17/04/2018.
 */

public class School implements Serializable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("zip_code")
    private String zipCode;
    @SerializedName("city")
    private String city;
    @SerializedName("opening_hours")
    private String openingHours;
    @SerializedName("student_number")
    private Integer numberStudent;
    @SerializedName("phone_number")
    private String phone;
    @SerializedName("status")
    private String status;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;

    public Integer getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Integer getNumberStudent() {
        return numberStudent;
    }

    public void setNumberStudent(Integer numberStudent) {
        this.numberStudent = numberStudent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
