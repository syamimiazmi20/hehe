package com.heroku.java.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.*;
import com.heroku.java.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerRegistrationController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/customerRegistration")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    @PostMapping("/CustomerLogin")
    public ResponseEntity<?> loginCustomer(@RequestParam String email, @RequestParam String password) {
        Customer customer = customerRepository.findByEmailAndPassword(email, password);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCustomers(@RequestParam String keyword) {
        List<Customer> customers = customerRepository.findByNameContaining(keyword);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/by-address")
    public ResponseEntity<?> getCustomersByAddress(@RequestParam String address) {
        List<Customer> customers = customerRepository.findByAddressContaining(address);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/by-phone")
    public ResponseEntity<?> getCustomerByPhone(@RequestParam String phone) {
        Customer customer = customerRepository.findByPhoneNum(phone);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count-by-address")
    public ResponseEntity<?> countCustomersByAddress(@RequestParam String address) {
        long count = customerRepository.countByAddress(address);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/all-ordered")
    public ResponseEntity<?> getAllCustomersOrdered() {
        List<Customer> customers = customerRepository.findAllOrderedByName();
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCustomerByEmail(@RequestParam String email) {
        customerRepository.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }
}