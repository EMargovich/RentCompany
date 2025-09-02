package com.telran.cars.dto;

import java.io.Serializable;
import java.util.Objects;

public class Model implements Serializable {
    private String modelName;
    private int gasTank;
    private String company;
    private String country;
    private int priceDay;

    public Model(String modelName, int gasTank, String company, String country, int priceDay) {
        this.modelName = modelName;
        this.gasTank = gasTank;
        this.company = company;
        this.country = country;
        this.priceDay = priceDay;
    }

    public Model() {
    }

    public String getModelName() {
        return modelName;
    }

    public int getGasTank() {
        return gasTank;
    }

    public String getCompany() {
        return company;
    }

    public String getCountry() {
        return country;
    }

    public int getPriceDay() {
        return priceDay;
    }

    public void setPriceDay(int priceDay) {
        this.priceDay = priceDay;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(modelName, model.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(modelName);
    }

    @Override
    public String toString() {
        return "Model{" +
                "modelName='" + getModelName() + '\'' +
                ", gasTank=" + getGasTank() +
                ", company='" + getCompany() + '\'' +
                ", country='" + getCountry() + '\'' +
                ", priceDay=" + getPriceDay() +
                '}';
    }
}
