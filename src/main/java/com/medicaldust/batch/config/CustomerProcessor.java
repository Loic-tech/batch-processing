package com.medicaldust.batch.config;

import com.medicaldust.batch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;


public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) {
        return customer;
    }
}
