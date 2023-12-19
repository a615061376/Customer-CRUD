package com.fenixs.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDao {

    List<CustomerEntity> selectAllCustomers();

    Optional<CustomerEntity> selectCustomerById(Long id);

    void insertCustomer(CustomerEntity customerEntity);

    boolean existPersonWithId(Long id);

    boolean existPersonWithEmail(String email);

    void updateCustomer(CustomerEntity updateObject);

    void deleteCustomerById(Long id);
}
