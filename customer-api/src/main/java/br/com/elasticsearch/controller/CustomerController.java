package br.com.elasticsearch.controller;

import br.com.elasticsearch.domain.Customer;
import br.com.elasticsearch.service.CustomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomeService customeService;

    @PostMapping("/register/bulk")
    @ResponseStatus(HttpStatus.OK)
    public void registerCustomerBulk(@RequestParam("persistence_type") String persistenceType) {
        if ("repository".equals(persistenceType)) {
            customeService.registerCustomerIndexBulkWithRepository();
        } else {
            customeService.registerCustomerIndexBulkWithElasticseartRestTemplate();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerCustomer(@RequestParam("persistence_type") String persistenceType, @RequestBody Customer customer) {
        if ("repository".equals(persistenceType)) {
            final Customer response = customeService.registerCustomerWithRepository(customer);
            return ResponseEntity.ok(response);
        }
        final String response = customeService.registerCustomerWithElasticseartRestTemplate(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/desegnation")
    public ResponseEntity<List<Customer>> findCustomerByDesignation(@RequestParam("designation") String designation) {
        final List<Customer> customerByDesignation = customeService.findCustomerByDesignation(designation);
        return ResponseEntity.ok(customerByDesignation);
    }

    @GetMapping("/lastName")
    public ResponseEntity<List<Customer>> findCustomerByLastName(@RequestParam("last_name") String lastName) {
        final List<Customer> customerByDesignation = customeService.findCustomerByLastName(lastName);
        return ResponseEntity.ok(customerByDesignation);
    }

    @GetMapping("/address")
    public ResponseEntity<List<Customer>> findCustomerByAddress(@RequestParam("address") String address) {
        final List<Customer> customerByDesignation = customeService.findCustomerByAddress(address);
        return ResponseEntity.ok(customerByDesignation);
    }

    @GetMapping("/salary")
    public ResponseEntity<List<Customer>> findCustomerByRangeSalary(@RequestParam("start_salary") Double startSalary,
                                                                    @RequestParam("end_salary") Double endSalary) {
        final List<Customer> customerByDesignation = customeService.findCustomerByRangeSalary(startSalary,endSalary);
        return ResponseEntity.ok(customerByDesignation);
    }

    @GetMapping("/name_or_last_name")
    public ResponseEntity<List<Customer>> processSearchName(@RequestParam("name_or_last_name") String nameOrLastName) {
        final List<Customer> customerByDesignation = customeService.processSearchName(nameOrLastName);
        return ResponseEntity.ok(customerByDesignation);
    }

    @GetMapping("/name/suggestions")
    public ResponseEntity<List<String>> processSearchNameSuggestions(@RequestParam("name") String name) {
        final List<String> namesSuggestions = customeService.processSearchNameSuggestions(name);
        return ResponseEntity.ok(namesSuggestions);
    }

}
