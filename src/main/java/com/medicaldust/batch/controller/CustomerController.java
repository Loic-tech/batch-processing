package com.medicaldust.batch.controller;

import com.medicaldust.batch.dto.CustomerRequest;
import com.medicaldust.batch.entity.Customer;
import com.medicaldust.batch.exception.UserNotFoundException;
import com.medicaldust.batch.repository.RangeRepository;
import com.medicaldust.batch.services.CustomersService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/jobs")
@RequiredArgsConstructor
public class CustomerController {

    private final JobLauncher jobLauncher;

    private final Job job;

    private final CustomersService service;

    private final RangeRepository repository;

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping(value = "importCustomers")
    public void importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "all")
    public ResponseEntity<List<Customer>> getCustomers(@RequestParam("page") String page, @RequestParam("size") String size) {
        int pageValue = Integer.parseInt(page);
        int sizeValue = Integer.parseInt(size);

        return new ResponseEntity<>(this.service.getCustomersSorted(pageValue, sizeValue), HttpStatus.OK);
    }

    @PostMapping(value = "add")
    public ResponseEntity<?> addNewCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        service.addNewCustomer(customerRequest);
        return new ResponseEntity<>(customerRequest, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        this.service.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "single-customer/{id}")
    public ResponseEntity<Customer> getSingleCustomer(@PathVariable Integer id) throws UserNotFoundException {
        Customer customer = this.service.getSingleCustomer(id);
        return new ResponseEntity<>(customer, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "country")
    public ResponseEntity<List<Customer>> getCustomersByCountries(@RequestParam("country") String countryName) {
       List<Customer> customerList = this.service.getCustomersByCountries(countryName);
       return new ResponseEntity<>(customerList, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "single-customer")
    public ResponseEntity<Object> getSingleCustomerByName(@RequestParam("name") String firstname) {
        Optional<Customer> optionalCustomer = this.service.getSingleCustomerByFirstname(firstname);

        return optionalCustomer
                .<ResponseEntity<Object>>map(customer -> new ResponseEntity<>(customer, HttpStatus.ACCEPTED))
                .orElseGet(() -> new ResponseEntity<>("NOT FOUND", HttpStatus.BAD_REQUEST));
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<Customer> putCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        Customer newCustomer = this.service.putCustomer(id, customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.ACCEPTED);
    }

    @PutMapping ("range/min")
    public ResponseEntity<?> getCustomersByRange(@RequestParam("min") int min) {
        return new ResponseEntity<>(this.repository.incrementValue(min) , HttpStatus.OK);
    }

/*    @GetMapping("range")
    public ResponseEntity<List<Customer>> getCustomersByRange(@RequestParam("min") int min, @RequestParam("max") int max) {
        return new ResponseEntity<>(this.service.getCustomersByRange(min, max) , HttpStatus.OK);
    }*/
}
