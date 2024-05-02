package ru.aston;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *The main class for the User Service application.
 *This class contains the main method to run the application.
 *@author [Ildar Nizamov]
 * */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    /**
     * The main method to start the User Service application.
     * @param args App run arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}