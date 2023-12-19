package com.fenixs.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    private static final List<CustomerEntity> customers;

    static {
        customers = new ArrayList<>();

        CustomerEntity alex = new CustomerEntity( "Alex", "alex@gmail.com", 21);
        CustomerEntity jamila = new CustomerEntity("jamila", "jamila@gmail.com", 19);
        customers.add(alex);
        customers.add(jamila);
    }

    @Override
    public List<CustomerEntity> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<CustomerEntity> selectCustomerById(Long id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(CustomerEntity customerEntity) {
        customers.add(customerEntity);
    }

    @Override
    public boolean existPersonWithId(Long id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void updateCustomer(CustomerEntity updateObject) {
        customers.add(updateObject);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }
}
