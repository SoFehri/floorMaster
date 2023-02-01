package com.tripledrift.flooringmastery.View;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tripledrift.flooringmastery.Model.Order;

public interface FloorMasterView
{
    void displayOrders(HashMap<LocalDate, HashMap<Integer, Order>> orderMap);

    void displayMessage(String msg);

    LocalDate getOrderDate(boolean futurDate);

     String getCustomerName();

     String getState(List<String> availableStates);
     
     String getProductType();

     BigDecimal getArea();

    void displayProducts(Map<String, BigDecimal[]> productMap);

    int getOrderNumb();

    String getUserYesOrNo(String string);

    int displayMenuAndGetSelection();

    void displayOrderSuccessBanner();

    LocalDate getDate();

    void displayEditErrorMsg();

     
}
