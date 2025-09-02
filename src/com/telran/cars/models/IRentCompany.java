package com.telran.cars.models;

import com.telran.cars.dto.*;
import com.telran.cars.dto.enums.CarsReturnCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public interface IRentCompany extends Serializable {

    int getGasPrice();

    void setGasPrice(int price);

    int getFinePercent();

    void setFinePercent(int finePercent);

    CarsReturnCode addModel (Model model);

    Model getModel(String modelName);

    CarsReturnCode addCar(Car car);

    Car getCar(String regNumber);

    CarsReturnCode addDriver(Driver driver);

    Driver getDriver(long licenseId);

    //Stream 2
    CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays);

    List<Car> getCarByDrivers (long licenseId);

    List<Driver> getDriversByCars (String regNumber);

    List<Car> getCarsByModel(String modelName);

    List<RentRecord> getRentRecordsAtDates(LocalDate from, LocalDate to);

    //Stream 3
    RemovedCarData removeCar(String regNumber);
    List<RemovedCarData> removeModel (String model);
    RemovedCarData returnCar(String regNumber, long licensedId,
                             LocalDate returnDate, int damages, int tankPercent);

}
