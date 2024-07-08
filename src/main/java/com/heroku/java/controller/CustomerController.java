package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.Customer;
import com.heroku.java.repository.CustomerRepository;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/index")
    public String index() {
        return "index"; // Refers to /src/main/resources/templates/index.jsp
    }

    @GetMapping("/customerRegistration")
    public String showRegistrationForm() {
        return "customerRegistration"; // Refers to /src/main/resources/templates/customerRegistration.jsp
    }

    @PostMapping("/registerCustomer")
    public String registerCustomer(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            Model model) {

        Customer customer = new Customer(name, email, password, address, phone);
        try {
            customerRepository.save(customer);
            return "redirect:/customerLogin"; // Refers to customerLogin.jsp
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while registering the customer. Please try again.");
            return "customerRegistration";
        }
    }

    @PostMapping("/authenticateCustomer")
    public String authenticateCustomer(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        Customer customer = customerRepository.authenticateCustomer(email, password);
        
        if (customer != null) {
            model.addAttribute("name", customer.getName());
            return "redirect:/dashboard"; // Refers to a dashboard or any other page you have
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/login"; // Refers to a login page
        }
    }
}
