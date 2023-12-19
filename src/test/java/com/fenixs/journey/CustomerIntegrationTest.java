package com.fenixs.journey;

import com.fenixs.customer.CustomerEntity;
import com.fenixs.customer.CustomerRegistrationRequest;
import com.fenixs.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient client;

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {
        // create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@fenxis.com";
        int age = RANDOM.nextInt(10, 100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        // send a post request
        client.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<CustomerEntity> allCustomers = client.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerEntity>() {})
                .returnResult()
                .getResponseBody();
        // make sure that customer is present
        CustomerEntity expect = new CustomerEntity(name, email, age);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expect);

        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        expect.setId(id);

        client.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerEntity>() {})
                .isEqualTo(expect);
    }

    @Test
    void canDeleteCustomer() {
        // create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@fenxis.com";
        int age = RANDOM.nextInt(10, 100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        // send a post request
        client.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<CustomerEntity> allCustomers = client.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerEntity>() {})
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        // delete customer
        client.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        client.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@fenxis.com";
        int age = RANDOM.nextInt(10, 100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        // send a post request
        client.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<CustomerEntity> allCustomers = client.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerEntity>() {})
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        // update customer
        String newName = "newName";
        CustomerUpdateRequest update = new CustomerUpdateRequest(
                newName, null, null
        );
        client.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(update), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        CustomerEntity actual = client.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerEntity.class)
                .returnResult()
                .getResponseBody();

        CustomerEntity expect = new CustomerEntity(id, newName, email, age);

        assertThat(actual).isEqualTo(expect);
    }
}
