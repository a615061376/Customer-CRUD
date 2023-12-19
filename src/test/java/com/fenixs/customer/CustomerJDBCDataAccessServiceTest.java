package com.fenixs.customer;

import com.fenixs.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainer {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper mapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJDBCTemplate(), mapper);
    }

    @Test
    void selectAllCustomers() {
        CustomerEntity customer = new CustomerEntity(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        List<CustomerEntity> actual = underTest.selectAllCustomers();
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();
        Optional<CustomerEntity> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void returnEmptyWhenSelectCustomerById() {
        Long id = -1L;
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        CustomerEntity customer = new CustomerEntity(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);


    }

    @Test
    void existPersonWithId() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();
        boolean actual = underTest.existPersonWithId(id);
        assertThat(actual).isTrue();
    }

    @Test
    void returnFalseWhenNotExistPersonWithId() {
        Long id = -1L;
        boolean actual = underTest.existPersonWithId(id);
        assertThat(actual).isFalse();
    }

    @Test
    void existPersonWithEmail() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        boolean actual = underTest.existPersonWithEmail(email);
        assertThat(actual).isTrue();
    }

    @Test
    void returnFalseWhenNotExistPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        boolean actual = underTest.existPersonWithEmail(email);
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        String fakeNewName = "faker";
        CustomerEntity updateObject = new CustomerEntity();
        updateObject.setId(id);
        updateObject.setName(fakeNewName);

        underTest.updateCustomer(updateObject);
        Optional<CustomerEntity> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(fakeNewName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        String fakeNewEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity updateObject = new CustomerEntity();
        updateObject.setId(id);
        updateObject.setEmail(fakeNewEmail);

        underTest.updateCustomer(updateObject);
        Optional<CustomerEntity> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(fakeNewEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        int fakeAge = 100;
        CustomerEntity updateObject = new CustomerEntity();
        updateObject.setId(id);
        updateObject.setAge(fakeAge);

        underTest.updateCustomer(updateObject);
        Optional<CustomerEntity> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(fakeAge);
        });
    }

    @Test
    void UpdateCustomerAllProperties() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        CustomerEntity updateObject = new CustomerEntity();
        updateObject.setId(id);
        updateObject.setName("fakeName");
        updateObject.setEmail(UUID.randomUUID().toString());
        updateObject.setAge(99);

        underTest.updateCustomer(updateObject);
        Optional<CustomerEntity> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(updateObject);
    }

    @Test
    void UpdateCustomerWhenNothingToUpdate() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();

        CustomerEntity updateObject = new CustomerEntity();
        updateObject.setId(id);

        underTest.updateCustomer(updateObject);
        Optional<CustomerEntity> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void deleteCustomerById() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity(name, email, 20);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(CustomerEntity::getId)
                .findFirst()
                .orElseThrow();
        underTest.deleteCustomerById(id);
        Optional<CustomerEntity> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }
}