package com.tripledrift.flooringmastery;

import com.tripledrift.flooringmastery.Controller.FloorMasterController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class FlooringmasteryApplication {

	public static void main(String[] args)
	{
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.scan("com.tripledrift.flooringmastery");
		appContext.refresh();

		FloorMasterController controller = appContext.getBean("floorMasterController", FloorMasterController.class);
		controller.run();
	}
}
