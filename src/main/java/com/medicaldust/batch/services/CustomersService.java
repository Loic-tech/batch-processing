package com.medicaldust.batch.services;

import com.medicaldust.batch.dto.CustomerRequest;
import com.medicaldust.batch.entity.Customer;
import com.medicaldust.batch.exception.UserNotFoundException;
import com.medicaldust.batch.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableCaching
@RequiredArgsConstructor
public class CustomersService {

    private final CustomerRepository repository;

    @Cacheable(value = "list")
    public List<Customer> getCustomersSorted(int page , int size) {
        Page<Customer> pageValue = this.repository.findAll(PageRequest.of(page, size));
        return pageValue.getContent();
    }

    public void addNewCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer
                .build(100002, customerRequest.getFirstname(), customerRequest.getLastname(),
                        customerRequest.getEmail(), customerRequest.getPhone(), customerRequest.getCountry());
        repository.save(customer);
    }

    public void deleteCustomer(Integer id) {
        repository.deleteById(id);
    }

    @Cacheable(value = "SingleCustomerByID")
    public Customer getSingleCustomer(Integer id) throws UserNotFoundException {
        Optional<Customer> optionalCustomer = repository.findById(id);

        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        else {
            throw new UserNotFoundException("USER NOT FOUND WITH ID  : " + id);
        }
    }

    public List<Customer> getCustomersByRange(int min , int max) {
        return this.repository.getAllCustomersByRange(min, max);
    }

    @Cacheable(value = "listByName")
    public List<Customer> getCustomersByCountries(String name) {
        if (name.length() > 0 ) {
            return this.repository.findAll()
                    .stream()
                    .filter(customer -> customer.getCountry().equals(name))
                    .collect(Collectors.toList());
        }
        else {
            System.out.println("COUNTRY NAME IS EMPTY");
            return null;
        }
    }

    @Cacheable(value = "SingleCustomerByName")
    public Optional<Customer> getSingleCustomerByFirstname(String name) {
        return Optional.ofNullable(this.repository.findCustomerByFirstname(name)
                .orElseThrow(RuntimeException::new));
    }

    public Customer putCustomer(Integer id, Customer newCustomer) {
        Optional<Customer> optionalCustomer = this.repository.findById(id);

        if (optionalCustomer.isEmpty()) {
            System.out.println("Customer not found");
            return null;
        }
        else {
            Customer oldCustomer = optionalCustomer.get();
            oldCustomer.setId(newCustomer.getId());
            oldCustomer.setFirstname(newCustomer.getFirstname());
            oldCustomer.setLastname(newCustomer.getLastname());
            oldCustomer.setEmail(newCustomer.getEmail());
            oldCustomer.setPhone(newCustomer.getPhone());
            oldCustomer.setCountry(newCustomer.getCountry());

            this.repository.save(oldCustomer);
            return oldCustomer;
        }
    }

}
