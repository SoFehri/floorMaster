package com.tripledrift.flooringmastery.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tripledrift.flooringmastery.Exception.FloorMasterPersistenceException;
import com.tripledrift.flooringmastery.Model.Order;

public interface FloorMasterService
{
    boolean removeOrder(LocalDate date, int orderNum);

    boolean validInputState(String state);

    boolean validInputProduct(String productType);

    Map<String,BigDecimal[]>  getProductMap();

    Order generateOrder(LocalDate date, String customerName, String state, 
                        String productType, BigDecimal area) throws IOException;

    void setMissingFields(Order order);

    void addOrder(LocalDate date, Order order);

    boolean existsOrder(LocalDate date, int orderNum);

    HashMap <LocalDate, HashMap<Integer, Order>> getOrdersMap();

    Order modifyOrder(LocalDate date, Order o) throws FloorMasterPersistenceException;

    Order generateOrderWithIDParameter(int orderNum, LocalDate date, String customerName, String state,
                                              String productType, BigDecimal area);
    boolean ordersMapEmpty();

    List<String> getListOfStates();
}
