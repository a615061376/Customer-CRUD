package com.fenixs.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    boolean existsCustomerEntityById(Long id);

    boolean existsCustomerEntityByEmail(String email);
}
