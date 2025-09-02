package com.telran.cars.dto;

import java.io.Serializable;
import java.util.Objects;

public class Driver implements Serializable {

    private long licenseId;
    private String name;
    private int birthYear;
    private String phone;

    public Driver(long licenseId, String name, int birthYear, String phone) {
        this.licenseId = licenseId;
        this.name = name;
        this.birthYear = birthYear;
        this.phone = phone;
    }

    public Driver() {
    }

    public long getLicenseId() {
        return licenseId;
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "licenseId=" + getLicenseId() +
                ", name='" + getName() + '\'' +
                ", birthYear=" + getBirthYear() +
                ", phone='" + getPhone() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return licenseId == driver.licenseId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(licenseId);
    }
}
