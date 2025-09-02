package com.telran.cars.dto;

import com.telran.cars.dto.enums.State;

import java.io.Serializable;
import java.util.Objects;

import static com.telran.cars.dto.enums.State.*;

public class Car implements Serializable {
    private String regNumber;
    private String color;
    private State state = EXCELLENT;
    private String modelName;
    private boolean inUse;
    private boolean flRemoved;

    public Car() {
    }

    public Car(String regNumber, String color, String modelName) {
        this.regNumber = regNumber;
        this.color = color;
        this.modelName = modelName;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public void setFlRemoved(boolean flRemoved) {
        this.flRemoved = flRemoved;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getColor() {
        return color;
    }

    public State getState() {
        return state;
    }

    public String getModelName() {
        return modelName;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean isFlRemoved() {
        return flRemoved;
    }

    @Override
    public String toString() {
        return "Car{" +
                "regNumber='" + getRegNumber() + '\'' +
                ", color='" + getColor() + '\'' +
                ", modelName='" + getModelName() + '\'' +
                ", inUse=" + isInUse() +
                ", flRemoved=" + isFlRemoved() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(regNumber, car.regNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(regNumber);
    }
}
