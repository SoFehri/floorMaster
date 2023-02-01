package com.tripledrift.flooringmastery.DAO;

import com.tripledrift.flooringmastery.Exception.FloorMasterPersistenceException;
import com.tripledrift.flooringmastery.Model.Order;
import org.springframework.stereotype.Component;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

@Component
public class FloorMasterDAOFileImpl implements FloorMasterDAO
{
    private HashMap<LocalDate, HashMap<Integer, Order>> orders;
    private Map<String, BigDecimal> taxMap;
    private  Map<String,BigDecimal[]> productMap;
    public static String directory = "Orders";
    public static String extension = ".txt";
    public static String header = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
            "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
    public static String dateFormat = "MMddyyyy";
    public static String delimiter = ",";
    public static String prefix = "Orders_";
    public static int orderNum;


    public FloorMasterDAOFileImpl() throws IOException
    {
        orders = new HashMap<>();
        List<LocalDate> dates = new ArrayList<>();

        List<String> fileNames = getFileNamesFromDirectory();
        for (String fileName: fileNames){
            dates.add(convertFileNameToDate(fileName));
        }
        populateOrdersMap(dates);
        taxMap = new HashMap<>();
        productMap = new HashMap<>();
        populateTaxAndProductMap("product.txt");
        populateTaxAndProductMap("tax.txt");
        readOrderNum();
    }

    public HashMap<LocalDate, HashMap<Integer, Order>> getOrdersMap(){
        return this.orders;
    }

    public void populateTaxAndProductMap(String fileName)
    {
        try
        {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            //skip the first line/header:
            myReader.nextLine();
            while (myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                String[] dataArr = data.split(",");
                if (fileName.equals("tax.txt"))
                {
                    taxMap.put(dataArr[0], new BigDecimal(dataArr[2]));

                }

                else if (fileName.equals("product.txt"))
                {
                    BigDecimal[] arrBigDecimals = {new BigDecimal(dataArr[1]), new BigDecimal(dataArr[2])};
                    productMap.put(dataArr[0], arrBigDecimals);
                }

            }
            myReader.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public Order addOrder(Order order, LocalDate date) throws FloorMasterPersistenceException
    {
        try
        {
            orders.computeIfAbsent(date, k -> new HashMap<Integer, Order>());
            orders.get(date).put(order.getOrderNum(), order);

            if (!existsFile(date))
            {
                createNewFile(date);
            }
            appendToFile(order, date);

            return order;
        }
        catch(Exception e){
            throw new FloorMasterPersistenceException("Unable to create new file", e);
        }
    }

    @Override
    public List<Order> getOrderList(LocalDate date)
    {
        return new ArrayList<>(orders.get(date).values());
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException
    {
        Order o =  orders.get(date).remove(orderNum);
        // delete the hashmap of a certain date AND delete the corresponding file
        // if that hashmap no longer contains any orders
        try
        {
            if (orders.get(date).isEmpty())
            {
                orders.remove(date);
                //if map empty then deleteFile()
                deleteFile(date);
            }
            else
            {
                //override and rewrite the contents to .txt file
                deleteFile(date);
                createNewFile(date);

                for (Order order: orders.get(date).values()){
                    appendToFile(order, date);
                }
            }
        }
        catch (Exception e)
        {
            throw new FloorMasterPersistenceException("Invalid date", e);
        }
        return o;
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws FloorMasterPersistenceException
    {
        Order o = orders.get(date).replace(order.getOrderNum(), order);
        //overwrite the txt file
        try
        {
            deleteFile(date);
            createNewFile(date);
            for (Order temp : orders.get(date).values())
            {
                appendToFile(temp, date);
            }
        }
        catch(Exception e){
            throw new FloorMasterPersistenceException("Invalid date", e);
        }
        return o;
    }

    @Override
    public boolean existsOrder(LocalDate date, int orderNum){
        return orders.get(date) != null && orders.get(date).get(orderNum) != null;
    }

    public boolean ordersMapEmpty(){
        return orders.isEmpty();
    }

    @Override
    public List<String> getListOfStates(){
        return new ArrayList<>(taxMap.keySet());
    }


    //private internal methods:
    private String serialize(Order o){
        return o.toString();
    }

    private Order deserialize(String s, LocalDate date){
        String[] strs = s.split(delimiter);
        return new Order(Integer.parseInt(strs[0]), strs[1], strs[2], new BigDecimal(strs[3]),
                strs[4], new BigDecimal(strs[5]), new BigDecimal(strs[6]), new BigDecimal(strs[7]),
                new BigDecimal(strs[8]), new BigDecimal(strs[9]), new BigDecimal(strs[10]), new BigDecimal(strs[11]));
    }

    private String formatDate(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    private void appendToFile(Order o, LocalDate date) throws IOException
    {
        PrintWriter writer = new PrintWriter(new FileWriter(getFilePathFromDate(date), true));
        writer.println(serialize(o));
        writer.flush();
        writer.close();
    }

    private void createNewFile(LocalDate date) throws IOException
    {
        Files.createDirectories(Paths.get(directory));

        PrintWriter writer = new PrintWriter(new FileWriter(getFilePathFromDate(date)));
        writer.println(header);
        writer.flush();
        writer.close();
    }

    private void deleteFile(LocalDate date) throws IOException
    {
        Files.delete(Paths.get(getFilePathFromDate(date)));
    }

    private boolean existsFile(LocalDate date){
        return (Files.isRegularFile(Paths.get(getFilePathFromDate(date))));
    }

    private String getFilePathFromDate(LocalDate date){
        String formattedDate = formatDate(date);
        return directory+File.separator+prefix+formattedDate+extension;
    }

    private List<String> getFileNamesFromDirectory() throws IOException
    {
        List<String> files = new ArrayList<>();
        if (Files.exists(Paths.get(directory)))
        {
            Stream<Path> stream = Files.list(Paths.get(directory));
            files = stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
        }
        return files;
    }

    private LocalDate convertFileNameToDate(String fileName){
        String s = fileName.substring(7,15);
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("MMddyyyy"));
    }

    private void populateOrdersMap(List<LocalDate> dates) throws FileNotFoundException
    {
        for (LocalDate date: dates){
            //Read the file for this corresponding date
            Scanner sc = new Scanner(new BufferedReader(new FileReader(getFilePathFromDate(date))));
            sc.nextLine(); //skip the header
            while (sc.hasNextLine()){
                String orderString = sc.nextLine();
                Order o = deserialize(orderString, date);
                orders.computeIfAbsent(date, k->new HashMap<Integer, Order>());
                orders.get(date).put(o.getOrderNum(), o);
            }
            sc.close();
        }
    }

    @Override
    public boolean validInputState(String state) {
        return taxMap.containsKey(state);

    }

    @Override
    public boolean validInputProduct(String product) {
        return productMap.containsKey(product);
    }

    @Override
    public Map<String,BigDecimal[]>  getProductMap() {
        return this.productMap;
    }

    @Override
    public BigDecimal getCostPerSqFoot(String product) {
        return  productMap.get(product)[0];
    }

    @Override
    public BigDecimal getLaborPerSqFoot(String product) {
        return productMap.get(product)[1];
    }

    @Override
    public BigDecimal getTaxRate(String state) {
        return taxMap.get(state);
    }


    public void readOrderNum() throws FileNotFoundException
    {
        Scanner sc = new Scanner(new BufferedReader(new FileReader("order_number.txt")));
        orderNum = sc.nextInt();
        sc.close();
    }

    @Override
    public int getNextOrderNumber() throws IOException
    {
        orderNum++;
        PrintWriter writer = new PrintWriter(new FileWriter("order_number.txt"));
        writer.println(orderNum);
        writer.flush();
        writer.close();
        return orderNum;
    }
}