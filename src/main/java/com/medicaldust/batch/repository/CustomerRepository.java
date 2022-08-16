package com.medicaldust.batch.repository;

import com.medicaldust.batch.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByFirstname(String firstname);

    @Query(value = "select * from customers LIMIT :min ,:max", nativeQuery = true)
    List<Customer> getAllCustomersByRange(int min, int max);
}
