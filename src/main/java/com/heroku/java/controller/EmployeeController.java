package com.heroku.java.controller;

import com.heroku.java.model.Employee;
import com.heroku.java.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody Employee employee) {
        // Validate role
        if (!employee.getRole().equalsIgnoreCase("admin") && !employee.getRole().equalsIgnoreCase("staff")) {
            return new ResponseEntity<>("Invalid role. Must be 'admin' or 'staff'.", HttpStatus.BAD_REQUEST);
        }

        // Check if email already exists
        if (employeeRepository.findByEmail(employee.getEmail()) != null) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable int id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<?> getEmployeesByRole(@PathVariable String role) {
        if (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("staff")) {
            return new ResponseEntity<>("Invalid role", HttpStatus.BAD_REQUEST);
        }
        List<Employee> employees = employeeRepository.findAll().stream()
                .filter(e -> e.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}