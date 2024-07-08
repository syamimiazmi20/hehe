package com.heroku.java.controller;

import com.heroku.java.model.Employee;
import com.heroku.java.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class adminLoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "adminLogin";
    }
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/adminLogin")
    public String showLoginForm() {
        return "adminLogin";
    }

    @PostMapping("/adminLogin")
    public String loginAdmin(@RequestParam String email, @RequestParam String password) {
        Employee admin = employeeRepository.findByEmail(email);
        if (admin != null && admin.getRole().equalsIgnoreCase("admin") && admin.getPassword().equals(password)) {
            // In a real application, you'd use proper password hashing and comparison
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/admin/login?error";
        }
    }

    @GetMapping("/adminRegistration")
    public String showRegistrationForm() {
        return "adminRegistration";
    }

    @PostMapping("/adminRegistration")
    public String registerAdmin(@ModelAttribute Employee admin) {
        if (!admin.getRole().equalsIgnoreCase("admin")) {
            return "redirect:/admin/register?error=invalidRole";
        }

        if (employeeRepository.findByEmail(admin.getEmail()) != null) {
            return "redirect:/admin/register?error=emailExists";
        }

        admin.setRole("admin"); // Ensure the role is set to admin
        employeeRepository.save(admin);
        return "redirect:/admin/login?registered";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "adminDashboard";
    }
}