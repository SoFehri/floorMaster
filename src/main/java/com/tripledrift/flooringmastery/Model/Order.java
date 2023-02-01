package com.tripledrift.flooringmastery.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order
{
    LocalDate date; //user input
    int orderNum; //generated
    String customerName; //user input
    String state;
    BigDecimal taxRate;
    String productType;
    BigDecimal costPerSqFoot;
    BigDecimal laborCostPerSqFoot;
    BigDecimal area; //user input
    BigDecimal materialCost; //internally calculated
    BigDecimal laborCost; //internally calculated
    BigDecimal tax; //internally calculated
    BigDecimal total; //internally calculated

    //Constructor for deserialization (does not include date)
    public Order(int orderNum, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSqFoot, BigDecimal laborCostPerSqFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total)
    {
        this.orderNum = orderNum;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.costPerSqFoot = costPerSqFoot;
        this.laborCostPerSqFoot = laborCostPerSqFoot;
        this.area = area;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    //Constructor that will take in an auto-generated order number as well as user inputs
    public Order(int orderNum, LocalDate date, String customerName, String state,
                 String productType, BigDecimal area){
        this.orderNum = orderNum;
        this.date = date;
        this.customerName = customerName;
        this.state = state;
        this.productType = productType;
        this.area = area;
    }

    //Constructor that includes all attributes
    public Order(LocalDate date, int orderNum, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal costPerSqFoot, BigDecimal laborCostPerSqFoot, BigDecimal area, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total)
    {
        this.date = date;
        this.orderNum = orderNum;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.costPerSqFoot = costPerSqFoot;
        this.laborCostPerSqFoot = laborCostPerSqFoot;
        this.area = area;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    public int getOrderNum()
    {
        return orderNum;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setTax(BigDecimal tax){
        this.tax = tax;
    }

   public String  getProduct(){
    return this.productType;
   }

    public String getState(){
        return this.state;
    }

    public BigDecimal getArea(){
        return this.area;
    }

    public void setArea(BigDecimal area){
        this.area = area;
    }

    public BigDecimal getMaterialCost(){
        return this.materialCost;
    }

    public BigDecimal getLaborCost(){
        return this.laborCost;
    }

    public BigDecimal getTax(){
        return this.tax;
    }

    public BigDecimal getTotal(){
        return this.total;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public void setCostPerSqFoot(BigDecimal costPerSqFoot) {
        this.costPerSqFoot = costPerSqFoot;
    }

    public void getLaborPerSqFoot(String product) {
        this.productType = product;
    }

    public void setLaborPerSqFoot(BigDecimal laborPerSqFoot) {
        this.laborCostPerSqFoot = laborPerSqFoot;
    }

    @Override
    public String toString()
    {
        return orderNum + "," +
                customerName + "," +
                state + "," +
                taxRate + "," +
                productType + "," +
                area + "," +
                costPerSqFoot + "," +
                laborCostPerSqFoot + "," +
                materialCost + "," +
                laborCost + "," +
                tax + "," +
                total;
    }
}
