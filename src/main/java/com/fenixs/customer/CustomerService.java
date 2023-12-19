package com.fenixs.customer;

import com.fenixs.exception.DuplicateResourceException;
import com.fenixs.exception.RequestValidationException;
import com.fenixs.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<CustomerEntity> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public CustomerEntity getCustomer(Long id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found.".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        String email = request.email();
        if (customerDao.existPersonWithEmail(email)) {
            throw new DuplicateResourceException("Email already taken");
        }
        String name = request.name();
        Integer age = request.age();
        CustomerEntity customer = new CustomerEntity(name, email, age);
        customerDao.insertCustomer(customer);
    }

    public void updateCustomer(Long id, CustomerUpdateRequest request) {

        CustomerEntity customer = getCustomer(id);

        boolean change = false;

        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            change = true;
        }

        if (request.email() != null && !request.email().equals(customer.getEmail())) {
            if (customerDao.existPersonWithEmail(request.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(request.email());
            change = true;
        }

        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            change = true;
        }

        if (!change) {
            throw new RequestValidationException("no data changes found!");
        }

        customerDao.updateCustomer(customer);
    }


    public void deleteCustomerById(Long id) {
        if(!customerDao.existPersonWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%s] not found.".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }


}
