package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.heroku.java.model.Employee;
import com.heroku.java.repository.EmployeeRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminRegistrationController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Employee employee) {
        employee.setRole("Admin");
        
        // Check if email already exists
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        try {
            Employee savedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while registering");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestParam String email, @RequestParam String password) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmailAndPassword(email, password);
        
        if (employeeOpt.isPresent()) {
            Employee admin = employeeOpt.get();
            if ("Admin".equals(admin.getRole())) {
                return ResponseEntity.ok(admin);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not an admin account");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}