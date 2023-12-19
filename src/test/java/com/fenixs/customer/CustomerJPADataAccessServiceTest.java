package com.fenixs.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();
        verify(repository).findAll();
    }

    @Test
    void selectCustomerById() {
        Long id = 1L;
        underTest.selectCustomerById(id);
        verify(repository).findById(id);
    }

    @Test
    void insertCustomer() {
        CustomerEntity mock = new CustomerEntity(1L, "faker", "faker@gamil.com", 3);
        underTest.insertCustomer(mock);
        verify(repository).save(mock);
    }

    @Test
    void existPersonWithId() {
        Long id = 1L;
        underTest.existPersonWithId(id);
        verify(repository).existsCustomerEntityById(id);
    }

    @Test
    void existPersonWithEmail() {
        String email = "faker@gamil.com";
        underTest.existPersonWithEmail(email);
        verify(repository).existsCustomerEntityByEmail(email);
    }

    @Test
    void updateCustomer() {
        CustomerEntity mock = new CustomerEntity(1L, "faker", "faker@gamil.com", 3);
        underTest.updateCustomer(mock);
        verify(repository).save(mock);
    }

    @Test
    void deleteCustomerById() {
        Long id = 1L;
        underTest.deleteCustomerById(id);
        verify(repository).deleteById(id);
    }
}