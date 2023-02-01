package com.tripledrift.flooringmastery.View;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tripledrift.flooringmastery.DAO.FloorMasterDAO;
import com.tripledrift.flooringmastery.Model.Order;
import com.tripledrift.flooringmastery.Service.FloorMasterService;

import ch.qos.logback.core.net.SyslogOutputStream;

import org.springframework.stereotype.Component;

@Component
public class FloorMasterViewImpl implements FloorMasterView
{
    UserIO io;

    public FloorMasterViewImpl(UserIO io)
    {
        this.io = io;
    }

    public LocalDate getOrderDate(boolean futureDate)
    {
        return (io.readDate("Please enter a date in the format yyyy-mm-dd", futureDate));
    }

    public String getCustomerName()
    {
        return (io.readCustomerName("Please enter your customer name"));
    }

    public String getState(List<String> availableStates)
    {
        return io.readString("Please enter your state two letter abbreviation. The available states are: "+
                availableStates);
    }

    public String getProductType()
    {
        return io.readString("Please select a product ");
    }

    public BigDecimal getArea()
    {
        return io.readArea("Please specify the area (minimum 100)");
    }

    @Override
    public void displayProducts(Map<String, BigDecimal[]> productMap)
    {
        for (String product : productMap.keySet())
        {
            io.print(product);
        }
    }

    @Override
    public int getOrderNumb()
    {
        return io.readInt("Enter order number");

    }

    public void displayMessage(String msg)
    {
        io.print(msg);
    }

    @Override
    public void displayOrders(HashMap<LocalDate, HashMap<Integer, Order>> ordersMap)
    {
        if (ordersMap.isEmpty())
            io.print("No orders to  display");

        else
        {
            LocalDate date = this.getOrderDate(false);
            //HashMap<Integer, Order> myMap = ordersMap.get(date);
            while (ordersMap.get(date) == null)
            {
                System.out.println("Invalid Date");
                date = this.getOrderDate(false);
            }

            for (Order order : ordersMap.get(date).values())
            {
                io.print(order.toString());
            }
        }
    }

    @Override
    public String getUserYesOrNo(String msg)
    {
        return io.readYesOrNo(msg);
    }

    @Override
    public int displayMenuAndGetSelection()
    {

        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");


        return io.readInt("Please select from the"
                + " above choices.", 1, 6);
    }

    @Override
    public void displayOrderSuccessBanner()
    {
        io.print("************************"); //view.printBanner();
        io.print("Order Placed Successfully !");
    }

    @Override
    public LocalDate getDate()
    {
        return io.readDate("Please enter your order's date", true);
    }

    @Override
    public void displayEditErrorMsg()
    {
        io.print("To modify an order, your date and order number must correspond to an existing order");
    }
}
