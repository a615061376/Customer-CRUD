package com.fenixs.customer;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerEntity> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<CustomerEntity> selectCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(CustomerEntity customerEntity) {
        customerRepository.save(customerEntity);
    }

    @Override
    public boolean existPersonWithId(Long id) {
        return customerRepository.existsCustomerEntityById(id);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customerRepository.existsCustomerEntityByEmail(email);
    }

    @Override
    public void updateCustomer(CustomerEntity updateObject) {
        customerRepository.save(updateObject);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
