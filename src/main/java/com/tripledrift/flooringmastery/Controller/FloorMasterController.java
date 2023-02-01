package com.tripledrift.flooringmastery.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.tripledrift.flooringmastery.Exception.FloorMasterPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import com.tripledrift.flooringmastery.Model.Order;
import com.tripledrift.flooringmastery.Service.FloorMasterService;
import com.tripledrift.flooringmastery.View.FloorMasterView;
import org.springframework.stereotype.Component;

@Component
public class FloorMasterController
{
    @Autowired
    FloorMasterView view;
    @Autowired
    FloorMasterService service;

    public void run()
    {
        boolean keepGoing = true;
        int menuSelection = 0;

        try
        {
            while (keepGoing)
            {
                menuSelection = displayMenuAndGetSelection();

                switch (menuSelection)
                {
                    case 1:
                        displayOrders();
                        break;

                    case 2:
                        addOrder();
                        break;

                    case 3:
                        modifyOrder();
                        break;

                    case 4:
                        removeOrder();
                        break;

                    case 5:
                        System.out.println("Cannot currently export data");
                        break;

                    case 6:
                        quit();
                        keepGoing = false;
                        break;

                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Controller Exception");
        }
    }

   private void quit(){
    System.out.println("GOOD BYE !");
   }

   private int displayMenuAndGetSelection() {
       return view.displayMenuAndGetSelection();
   }
   
   private void removeOrder() {
       if (service.ordersMapEmpty()){
           System.out.println("There are no orders available to remove");
           return;
       }
        boolean removeAnotherOrder = true;
        String yesOrNo;
        while(removeAnotherOrder){
            LocalDate date = view.getOrderDate(false);
            int orderNumb = view.getOrderNumb();
            if (service.removeOrder(date, orderNumb))
            {
                view.displayMessage("**************************");
                view.displayMessage("Order Removed Successfully !");
            }
            yesOrNo = view.getUserYesOrNo("Would you like to remove an order Yes/No ? ");
            if(yesOrNo.equals("NO"))
                removeAnotherOrder = false;
        }
    }

    private void displayOrders(){
        view.displayOrders(service.getOrdersMap());
    }

    private void addOrder() throws IOException
    {
        boolean addAnotherOrder = true;
        String yesOrNo;
        while(addAnotherOrder){
            Order order = getUserInput();
            setMissingFields(order);
            service.addOrder(order.getDate(), order);
            displayOrderSuccessBanner();
            yesOrNo = view.getUserYesOrNo("Would you like to place another order, YES/NO ?");
            if (yesOrNo.equals("NO"))
                addAnotherOrder = false;
        }
    }

    private void setMissingFields(Order order) {
        service.setMissingFields(order);
    }

    private Order generateOrder(LocalDate date, String customerName, String state, 
                               String productType, BigDecimal area) throws IOException
    {
       
        return service.generateOrder(date, customerName, state, productType, area);
    }

    private Order modifyOrder() throws FloorMasterPersistenceException
    {
        if (service.ordersMapEmpty()){
            System.out.println("There are no orders available for modification");
            return null;
        }
        while(true)
        {
            LocalDate date = view.getOrderDate(true);
            int orderNum = getOrderNumber();
            if (!service.existsOrder(date, orderNum)){
                displayEditErrorMsg();
                continue;
            }
            String customerName = view.getCustomerName();
            BigDecimal area = view.getArea();
            String state = getInputState();
            String productType = getProductInput();

            Order o = service.generateOrderWithIDParameter(orderNum, date, customerName, state, productType, area);
            service.setMissingFields(o);
            return service.modifyOrder(o.getDate(), o);
        }
    }

    private Order getUserInput() throws IOException
    {
        LocalDate date  = view.getOrderDate(true);
        String customerName = view.getCustomerName();
        BigDecimal area = view.getArea();
        String state = getInputState();
        String productType = getProductInput();
       return  generateOrder(date, customerName, state, productType, area);
    }

    private String getProductInput() {
        Map<String,BigDecimal[]> productMap = service.getProductMap();
        view.displayProducts(productMap); 
       String productType;
        do{
            productType = view.getProductType();
            } while(!validInputProduct(productType));
        return productType;
    }

    private String getInputState(){
        String state;
        List<String> availableStates = service.getListOfStates();
        do{
            state = view.getState(availableStates);
          } while(!validInputState(state));
        return state;
    }

    private boolean validInputState(String state) {
        return service.validInputState(state);
    }

    private boolean validInputProduct(String productType) {
        return service.validInputProduct(productType);
    }
    
    private void displayOrderSuccessBanner() {
        view.displayOrderSuccessBanner();
    }
    
    private LocalDate getDate() {
        return view.getDate();
    }
    
    private int getOrderNumber(){
        return view.getOrderNumb();
    }
    
    private void displayEditErrorMsg() {
        view.displayEditErrorMsg();
    }
}
