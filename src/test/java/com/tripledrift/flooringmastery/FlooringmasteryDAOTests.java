package com.tripledrift.flooringmastery;

import com.tripledrift.flooringmastery.DAO.FloorMasterDAOFileImpl;
import com.tripledrift.flooringmastery.Exception.FloorMasterPersistenceException;
import com.tripledrift.flooringmastery.Model.Order;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FlooringmasteryDAOTests
{
	FloorMasterDAOFileImpl testDao;

	@BeforeEach
	void init() throws IOException
	{
		//deleteOrdersFiles();
		testDao = new FloorMasterDAOFileImpl();
	}

	Order createOrder1(){
		return new Order(LocalDate.of(2022,12,25),1,"Mike Lee","WA",new BigDecimal("9.25"),"Wood",
				new BigDecimal("243.00"), new BigDecimal("5.15"),
				new BigDecimal("4.75"), new BigDecimal("1251.45"),
				new BigDecimal("1154.25"),new BigDecimal("216.51"), new BigDecimal("2622.21"));
	}

	Order createEditedOrder1(){
		return new Order(LocalDate.of(2022,12,25),1,"John","WA",new BigDecimal("9.25"),"Wood",
				new BigDecimal("243.00"), new BigDecimal("5.15"),
				new BigDecimal("4.75"), new BigDecimal("1251.45"),
				new BigDecimal("1154.25"),new BigDecimal("216.51"), new BigDecimal("2622.21"));
	}

	Order createOrder2(){
		return new Order(LocalDate.of(2022,12,25),2,"Sarah B","NY",new BigDecimal("11.5"),"Carpet",
				new BigDecimal("217"), new BigDecimal("2.25"),
				new BigDecimal("2.10"), new BigDecimal("488.25"),
				new BigDecimal("455.70"),new BigDecimal("56.64"), new BigDecimal("1000.59"));
	}

	Order createOrder3(){
		return new Order (LocalDate.of(2022,12,25),3,"Jackson","TX",new BigDecimal("11.5"),"Carpet",
				new BigDecimal("217"), new BigDecimal("2.25"),
				new BigDecimal("2.10"), new BigDecimal("488.25"),
				new BigDecimal("455.70"),new BigDecimal("56.64"), new BigDecimal("1000.59"));
	}

	@Test
	void addingOneOrder() throws FloorMasterPersistenceException
	{
		Order o = createOrder1();

		testDao.addOrder(o, o.getDate());

		assert (o == (testDao.getOrdersMap().get(o.getDate()).get(o.getOrderNum())));
	}

	@Test
	void modifyingTheOrder() throws FloorMasterPersistenceException
	{
		Order o1 = createOrder1();
		testDao.addOrder(o1, o1.getDate());


		Order o2 = createEditedOrder1();
		testDao.editOrder(o1.getDate(), o2);

		assert(o2 == testDao.getOrdersMap().get(o2.getDate()).get(o2.getOrderNum()));

	}

	@Test
	void verifyOrders() throws FloorMasterPersistenceException
	{
		Order o1 = createOrder1(); //order num 1
		Order o2 = createOrder2(); //order num 2
		Order o3 = createOrder3(); //order num 3

		List<Order> orders = new ArrayList<>(Arrays.asList(o1,o2,o3));

		testDao.addOrder(o1, o1.getDate());
		testDao.addOrder(o2, o2.getDate());
		testDao.addOrder(o3, o3.getDate());


		List<Order> orderList = testDao.getOrderList(o1.getDate()); //All three orders have the same date

		orderList.sort(new Comparator<Order>()
		{
			@Override
			public int compare(Order o1, Order o2)
			{
				return o1.getOrderNum() - o2.getOrderNum();
			}
		});
		assert(orders.equals(orderList));
	}

	@Test
	void deleteAnOrder() throws FloorMasterPersistenceException
	{
		Order o1 = createOrder1();
		Order o2 = createOrder2();
		testDao.addOrder(o1, o1.getDate());
		testDao.addOrder(o2, o2.getDate());

		testDao.removeOrder(o1.getDate(), o1.getOrderNum());

		List<Order> orders = testDao.getOrderList(o1.getDate()); //dates for o1 and o2 are the same

		assert (o2 == orders.get(0));
	}

	@AfterEach
	void deleteOrdersFiles() throws IOException
	{
		FileUtils.deleteDirectory(new File(FloorMasterDAOFileImpl.directory));
	}
}
