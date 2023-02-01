package com.tripledrift.flooringmastery.DAO;

import com.tripledrift.flooringmastery.Exception.FloorMasterPersistenceException;
import com.tripledrift.flooringmastery.Model.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FloorMasterDAO
{
    Order addOrder(Order o, LocalDate date) throws IOException, FloorMasterPersistenceException;

    List<Order> getOrderList (LocalDate date);

    Order removeOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException;

    Order editOrder (LocalDate date, Order order) throws IOException, FloorMasterPersistenceException;

    boolean validInputState(String state);

    boolean validInputProduct(String product);

    void populateTaxAndProductMap(String fileName);

    Map<String,BigDecimal[]>  getProductMap();

    HashMap<LocalDate, HashMap<Integer, Order>> getOrdersMap();

    BigDecimal getCostPerSqFoot(String product);

    BigDecimal getLaborPerSqFoot(String product);

    BigDecimal getTaxRate(String state);

    boolean existsOrder(LocalDate date, int orderNum);

    boolean ordersMapEmpty();

    List<String> getListOfStates();

    int getNextOrderNumber() throws IOException;
}

