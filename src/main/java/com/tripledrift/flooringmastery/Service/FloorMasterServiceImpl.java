package com.tripledrift.flooringmastery.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tripledrift.flooringmastery.DAO.FloorMasterDAO;
import com.tripledrift.flooringmastery.DAO.FloorMasterDAOFileImpl;
import com.tripledrift.flooringmastery.Exception.FloorMasterPersistenceException;
import com.tripledrift.flooringmastery.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FloorMasterServiceImpl implements FloorMasterService
{
    @Autowired
    FloorMasterDAO dao;

    public FloorMasterServiceImpl()
    {
    }

    //JUnit does not perform dependency injection, so use this constructor instead
    public FloorMasterServiceImpl(FloorMasterDAOFileImpl dao){
        this.dao = dao;
    }

    @Override
    public boolean validInputState(String state) {
        return dao.validInputState(state);
    }

    @Override
    public boolean validInputProduct(String productType) {
        return dao.validInputProduct(productType);    
    }

    @Override
    public  Map<String,BigDecimal[]> getProductMap() {
        return dao.getProductMap();      
    }

    @Override
    public List<String> getListOfStates(){
        return dao.getListOfStates();
    }

    //will internally generate ID
    @Override
    public Order generateOrder(LocalDate date, String customerName, String state, 
                               String productType, BigDecimal area) throws IOException
    {
        //auto generate ID
        int orderNum = dao.getNextOrderNumber();

        return new Order(orderNum, date, customerName, state, productType, area);
    }

    public Order generateOrderWithIDParameter(int orderNum, LocalDate date, String customerName, String state,
                                              String productType, BigDecimal area){
        return new Order(orderNum, date, customerName, state, productType, area);
    }

    @Override
    public void setMissingFields(Order order) {
        String product = order.getProduct();
        BigDecimal area = order.getArea();
        String state = order.getState();
       
        BigDecimal taxRate = dao.getTaxRate(state);
        BigDecimal costPerSqFoot = dao.getCostPerSqFoot(product); 
        BigDecimal laborPerSqFoot = dao.getLaborPerSqFoot(product);

        BigDecimal  materialCost = calculateMaterialCost(costPerSqFoot, area);
        BigDecimal laborCost = calculateLaborCost(laborPerSqFoot, area);
        BigDecimal tax = calculateTax(materialCost, laborCost, taxRate);
        BigDecimal total = calculateTotal(materialCost, laborCost, tax);

        order.setMaterialCost(materialCost);
        order.setTax(tax);
        order.setLaborCost(laborCost);
        order.setTotal(total);
        order.setTaxRate(taxRate);
        order.setCostPerSqFoot(costPerSqFoot);
        order.setLaborPerSqFoot(laborPerSqFoot);
    }

    public boolean removeOrder(LocalDate date, int orderNum){
         try{
            if (dao.removeOrder(date,orderNum) == null){
                System.out.println("Your order number does not exist. Please try again.");
                return false;
            }
            return true;
         }
         catch (Exception e){
            System.out.println("Your date was invalid. Please try again.");
            return false;
         }
    }

    @Override
    public boolean existsOrder(LocalDate date, int orderNum){
        return dao.existsOrder(date, orderNum);
    }

    private BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax);
    }

    private BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return (materialCost.add(laborCost)).multiply(tax).divide(new BigDecimal(100));
    }

    private BigDecimal calculateLaborCost(BigDecimal laborPerSqFoot, BigDecimal area) {
        return laborPerSqFoot.multiply(area);
    }

    private BigDecimal calculateMaterialCost(BigDecimal costPerSqFoot, BigDecimal area) {
        return costPerSqFoot.multiply(area);
    }

    @Override
    public void addOrder(LocalDate date, Order order) {
        try{dao.addOrder(order,date);
        } catch (Exception e ){
            System.out.println("Error while adding order " + e );
        }
    }

    @Override
    public HashMap <LocalDate, HashMap<Integer, Order>> getOrdersMap() {
        return dao.getOrdersMap();        
    }

    @Override
    public Order modifyOrder(LocalDate date, Order o) throws FloorMasterPersistenceException
    {
        try
        {
            return dao.editOrder(date, o);
        }catch(IOException e){
            throw new FloorMasterPersistenceException("Could not edit order", e);
        }
    }

    public boolean ordersMapEmpty(){
        return dao.ordersMapEmpty();
    }
}
