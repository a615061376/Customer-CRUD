package com.fenixs;

import com.fenixs.customer.CustomerEntity;
import com.fenixs.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    CommandLineRunner runner(CustomerRepository customerRepository) {
//        return args -> {
//            Faker faker = new Faker();
//            Name name = faker.name();
//            String firstName = name.firstName();
//            String lastName = name.lastName();
//            CustomerEntity fakeCustomer = new CustomerEntity(
//                    firstName + " " + lastName,
//                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@fenixs.com",
//                    new Random().nextInt(10,100));
//            customerRepository.save(fakeCustomer);
//        };
//    }
}
