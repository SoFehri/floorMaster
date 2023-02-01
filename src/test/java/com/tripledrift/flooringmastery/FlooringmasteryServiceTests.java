package com.tripledrift.flooringmastery;

import com.tripledrift.flooringmastery.DAO.FloorMasterDAO;
import com.tripledrift.flooringmastery.DAO.FloorMasterDAOFileImpl;
import com.tripledrift.flooringmastery.Model.Order;
import com.tripledrift.flooringmastery.Service.FloorMasterService;
import com.tripledrift.flooringmastery.Service.FloorMasterServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FlooringmasteryServiceTests
{
    FloorMasterService service;

    @BeforeEach
    void setUp() throws IOException
    {
        //deleteOrdersFiles();
        FloorMasterDAOFileImpl dao = new FloorMasterDAOFileImpl();
        service = new FloorMasterServiceImpl(dao);
    }

    @Test
    void generateAnOrder(){
        Order o = service.generateOrderWithIDParameter(1, LocalDate.now(),
                "John", "CA", "Wood", new BigDecimal("100"));

        service.setMissingFields(o);

        assertEquals(o.getLaborCost(), new BigDecimal("475.00"));
        assertEquals(o.getMaterialCost(), new BigDecimal("515.00"));
        assertEquals(o.getTax(),new BigDecimal("247.5000"));
        assertEquals(o.getTotal(), new BigDecimal("1237.5000"));
    }

    @Test
    void removeOrderWithInvalidDate(){
        //Any date should return false because the current hashmap is empty
        assertFalse(service.removeOrder(LocalDate.now(), -1));
    }

    @Test
    void removeValidOrder(){
        Order o = service.generateOrderWithIDParameter(1, LocalDate.now(),
                "John", "CA", "Wood", new BigDecimal("100"));
        service.addOrder(o.getDate(), o);
        service.removeOrder(o.getDate(), o.getOrderNum());
        assertTrue(service.ordersMapEmpty());
    }

    @AfterEach
    void deleteOrdersFiles() throws IOException
    {
        FileUtils.deleteDirectory(new File(FloorMasterDAOFileImpl.directory));
    }
}
