package com.fenixs.customer;

import com.fenixs.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainer {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setup() {
        underTest.deleteAll();
        System.out.println(context.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerEntityById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        Long id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();
        boolean actual = underTest.existsCustomerEntityById(id);
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerEntityByIdFailsWhenIdNotPresent() {
        Long id = -1L;
        boolean actual = underTest.existsCustomerEntityById(id);
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerEntityByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        Long id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();
        boolean actual = underTest.existsCustomerEntityByEmail(email);
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerEntityByEmailFailedWhenEmailNotPresent() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        boolean actual = underTest.existsCustomerEntityByEmail(email);
        assertThat(actual).isFalse();
    }
}