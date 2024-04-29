package ru.aston;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *The main class for the Order Service application.
 *This class contains the main method to run the application.
 *@author [Ildar Nizamov]
 * */
@SpringBootApplication
public class OrderServiceApplication {

	/**
	 * The main method to start the Order Service application.
	 * @param args App run arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}