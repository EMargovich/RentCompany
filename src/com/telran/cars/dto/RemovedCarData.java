package com.telran.cars.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RemovedCarData implements Serializable {
    Car car;
    List<RentRecord> removedRecords;

    public Car getCar() {
        return car;
    }

    public List<RentRecord> getRemovedRecords() {
        return removedRecords;
    }

    public RemovedCarData(Car car, List<RentRecord> removedRecords) {
        this.car = car;
        this.removedRecords = removedRecords;
    }

    public RemovedCarData() {
    }

    @Override
    public String toString() {
        return "RemoveCarData{" +
                "car=" + getCar() +
                ", removedRecords=" + getRemovedRecords() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RemovedCarData that = (RemovedCarData) o;
        return Objects.equals(car, that.car) && Objects.equals(removedRecords, that.removedRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, removedRecords);
    }
}
