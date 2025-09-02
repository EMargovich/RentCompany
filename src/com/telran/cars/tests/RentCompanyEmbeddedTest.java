package com.telran.cars.tests;

import com.telran.cars.dto.*;
import com.telran.cars.dto.enums.State;
import com.telran.cars.models.IRentCompany;
import com.telran.cars.models.RentCompanyEmbedded;
import com.telran.utils.Persistable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.telran.cars.dto.enums.CarsReturnCode.*;
import static org.junit.jupiter.api.Assertions.*;

public class RentCompanyEmbeddedTest {
    final String MODEL_NAME = "Model1";
    final int GAS_TANK = 50;
    final int PRICE_PER_DAY = 200;
    final long LICENSE = 1000L;
    final String REG_NUMBER = "100";
    final String COMPANY = "Company";
    final String COUNTRY = "Country1";
    final String COLOR = "black";
    final String NAME = "name1";
    final int YEAR_OB = 1990;
    final String PHONE_NUMBER = "123456789";
    final LocalDate RENT_DATE = LocalDate.of(2025, 8, 1);
    final LocalDate RETURN_DATE = LocalDate.of(2025, 8, 6);
    final int RENT_DAYS = 3;
    final int DAMAGES = 5;
    final int TANK_PERCENT = 80;
    final int COST = 1160;
    final int REMOVE_TRESHOLD = 60;
    final int BAD_TRESHOLD = 30;
    final int GOOD_TRESHOLD = 10;


    private Model model;
    private Car car;
    private Driver driver;

    private IRentCompany company;


    @BeforeEach
    void setUp() {
        model = new Model(MODEL_NAME, GAS_TANK, COMPANY, COUNTRY, PRICE_PER_DAY);
        car = new Car(REG_NUMBER, COLOR, MODEL_NAME);
        driver = new Driver(LICENSE, NAME, YEAR_OB, PHONE_NUMBER);
        company = new RentCompanyEmbedded();
        ((Persistable) company).save("company2.dat");
    }

    @Test
    void testAdd_get_Entities_OK() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(model, company.getModel(MODEL_NAME));
        assertEquals(car, company.getCar(REG_NUMBER));
        assertEquals(driver, company.getDriver(LICENSE));
    }

    @Test
    void testAdd_Duplicate_Entities_OK() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(MODEL_EXISTS, company.addModel(model));
        assertEquals(CAR_EXISTS, company.addCar(car));
        assertEquals(DRIVER_EXISTS, company.addDriver(driver));
    }

    @Test
    void test_get_when_not_added() {
        assertNull(company.getCar(REG_NUMBER));
        assertNull(company.getDriver(LICENSE));
        assertNull(company.getModel(MODEL_NAME));
    }


    @Test
    void testSaveRestore_OK() {

        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        String file = "companyTest.data";

        ((Persistable) company).save(file);
        IRentCompany restored = RentCompanyEmbedded.restoreFromFile(file);
        assertNotNull(restored);

        assertEquals(model, restored.getModel(MODEL_NAME));
        assertEquals(car, restored.getCar(REG_NUMBER));
        assertEquals(driver, restored.getDriver(LICENSE));
    }

    @Test
    void testSaveRestore_Fail() {
        String file = "companyTest_file.data";
        IRentCompany restored = RentCompanyEmbedded.restoreFromFile(file);
        assertNotNull(restored);
        assertTrue(restored instanceof RentCompanyEmbedded);

        assertNull(restored.getModel(MODEL_NAME));
        assertNull(restored.getCar(REG_NUMBER));
        assertNull(restored.getDriver(LICENSE));
    }

    //Sprint 2
    @Test
    void testRentCarOK() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(OK, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }
    @Test
    void testRentCarNO_CAR() {
        assertEquals(OK, company.addModel(model));
        //assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(NO_CAR, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }
        @Test
    void testRentCarNO_DRIVER() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        //assertEquals(OK, company.addDriver(driver));

        assertEquals(NO_DRIVER, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentCarRemove_Use() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        car.setFlRemoved(true);
        assertEquals(CAR_REMOVED, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));

        car.setFlRemoved(false);
        car.setInUse(true);
        assertEquals(CAR_IN_USE, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentRecords() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        RentRecord expected = new RentRecord(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        List<RentRecord> res = company.getRentRecordsAtDates(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS));
        assertEquals(1, res.size());
        assertEquals(expected, res.get(0));

        List<RentRecord> res1 = company.getRentRecordsAtDates(RENT_DATE.plusDays(RENT_DAYS), RENT_DATE.plusDays(10));
        assertTrue(res1.isEmpty());


    }

    @Test
    void testCarsByDriver() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        List<Car> res = company.getCarByDrivers(LICENSE);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        res = company.getCarByDrivers(LICENSE+1);
        assertTrue(res.isEmpty());
    }

    @Test
    void testDriversByCar() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        List<Driver> res = company.getDriversByCars(REG_NUMBER);
        assertEquals(1, res.size());
        assertEquals(driver, res.get(0));

        res = company.getDriversByCars(REG_NUMBER+1);
        assertTrue(res.isEmpty());
    }

    @Test
    void testCarByModel() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        List<Car> res = company.getCarsByModel(MODEL_NAME);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        Car car1 = new Car(REG_NUMBER+1, COLOR+"R", MODEL_NAME);
        assertEquals(OK, company.addCar(car1));
        car1.setInUse(true);
        res = company.getCarsByModel(MODEL_NAME);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        Car car2 = new Car(REG_NUMBER+2, COLOR+"R",MODEL_NAME);
        assertEquals(OK, company.addCar(car2));
        car2.setInUse(true);
       // assertEquals(CAR_IN_USE, company);
    }

    @Test
    void testRemoveCarInUse() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        assertEquals(new RemovedCarData(car, null), company.removeCar(REG_NUMBER));

        assertNull(company.removeCar(REG_NUMBER+100));
        car.setFlRemoved(true);

        RemovedCarData nC1 = new RemovedCarData(car, new ArrayList<>());
        assertNull(company.removeCar(REG_NUMBER));
    }

    @Test
    void testRemoveCarNotInUse() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        RemovedCarData nC = new RemovedCarData(car, new ArrayList<>());
        assertEquals(nC, company.removeCar(REG_NUMBER));
        //Написать тест, когда удаляется машина, у которой есть история.
        //Проверить, возвращается ли список и удаляется ли из всех мэп
    }

    @Test
    void testRemoveCarPositive() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        //Формирую полную запись об аренде и возврате в компании
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE,DAMAGES, TANK_PERCENT);

        List<RentRecord> listRecordsInCompany =  company.getRentRecordsAtDates(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS));

        //Описываю ожидаемую запись
        RentRecord record = new RentRecord(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        record.setReturnDate(RETURN_DATE);
        record.setDamages(DAMAGES);
        record.setTankPercent(TANK_PERCENT);
        record.setCost(COST);

        //Проверяю заполненность мэп до удаления. Это не нужно, но для понимания
        assertEquals(record, listRecordsInCompany.get(0));                  //map records
        assertEquals(car, company.getCar(REG_NUMBER));                      //map cars
        assertEquals(car, company.getCarsByModel(MODEL_NAME).get(0));       //map modelCars

        //Описываю ожидаемое возвращаемое значение при удалении
        RemovedCarData expectedRemoveCarData = new RemovedCarData(car, listRecordsInCompany);

        //Удаляю машину
        RemovedCarData actualRemoveCarData = company.removeCar(REG_NUMBER);
        assertEquals(expectedRemoveCarData, actualRemoveCarData);       //Проверяю возвращаемое значение

        //Проверяю, что записи стерты из мэп
        List<RentRecord> listRecordsInCompany2 =  company.getRentRecordsAtDates(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS));
        assertTrue(listRecordsInCompany2.isEmpty());                    //map records
        assertNull(company.getCar(REG_NUMBER));                         //map cars
        assertTrue(company.getCarsByModel(MODEL_NAME).isEmpty());       //map modelCars
    }

    @Test
    void testReturnCarNoRecord() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        RemovedCarData expected = new RemovedCarData(null,null);
        RemovedCarData actual = company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE,DAMAGES, TANK_PERCENT);

        assertEquals(expected, actual);
    }

    @Test void testReturnCarUpdateRecords() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        //Формирую полную запись об аренде и возврате в компании
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        RentRecord expectedRecord = new RentRecord(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        RentRecord actualRecord = company.getRentRecordsAtDates(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS)).get(0);
        assertEquals(expectedRecord, actualRecord);

        company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE,DAMAGES, TANK_PERCENT);
        actualRecord = company.getRentRecordsAtDates(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS)).get(0);

        expectedRecord.setReturnDate(RETURN_DATE);
        expectedRecord.setDamages(DAMAGES);
        expectedRecord.setTankPercent(TANK_PERCENT);
        expectedRecord.setCost(COST);

        assertEquals(expectedRecord, actualRecord);
    }

    @Test
    void testReturnCarUpdateCar() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(State.EXCELLENT, car.getState());

        RemovedCarData expected = new RemovedCarData(car, null);
        RemovedCarData actual;

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        actual = company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE, DAMAGES, TANK_PERCENT); //5%
        assertEquals(State.EXCELLENT, car.getState());
        assertEquals(expected, actual);

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        actual = company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE, GOOD_TRESHOLD + 5, TANK_PERCENT); //15%
        assertEquals(State.GOOD, car.getState());
        assertEquals(expected, actual);

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        actual = company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE, BAD_TRESHOLD + 5, TANK_PERCENT); //15%
        assertEquals(car, company.getCar(REG_NUMBER));
        assertEquals(State.BAD, car.getState());
        assertEquals(expected, actual);
    }

    @Test
    void testReturnCarRemoveCarDamage() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        RemovedCarData actual = company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE, REMOVE_TRESHOLD + 5, TANK_PERCENT); //65%

        assertNull(company.getCar(REG_NUMBER));
        assertEquals(1, actual.getRemovedRecords().size());
    }

    @Test
    void testReturnCarRemoveIsFlRemoved() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        assertFalse(company.getCar(REG_NUMBER).isFlRemoved());

        //car.setFlRemoved(true);
        company.removeCar(REG_NUMBER); //Установка признака на удаление для машины, которая выдана клиенту
        assertEquals(car, company.getCar(REG_NUMBER));
        assertTrue(company.getCar(REG_NUMBER).isFlRemoved());

        RemovedCarData actual = company.returnCar(REG_NUMBER, LICENSE, RETURN_DATE, GOOD_TRESHOLD + 5, TANK_PERCENT); //65%

        assertNull(company.getCar(REG_NUMBER));
        assertEquals(1, actual.getRemovedRecords().size());
    }

    @Test
    void testRemoveModel() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        Car car2 = new Car(REG_NUMBER + 2, COLOR, MODEL_NAME);

        company.addCar(car2);
        assertEquals(2,company.getCarsByModel(MODEL_NAME).size());

        //Значение этого признака мне в данном случае не понятно. Возможно, у меня ошибка в самом методе.
        //Пока получается, что удаление по модели проходит только в случае установки признака к удалению.
        //Но признак на удаление ставится только на выданную клиенту машину.
        //Тогда получается, что удалить все модели одной марки мы можем только в случае,
        // если их пытались удалить, но они были на руках. Что, вроде, противоречит хозяйственному смыслу
        car.setFlRemoved(true);
        car2.setFlRemoved(true);

        company.removeModel(MODEL_NAME);
        assertEquals(0,company.getCarsByModel(MODEL_NAME).size());
    }
}
