package com.heroku.java.controller;

import com.heroku.java.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/CustomerRegistration")
public class CustomerController {


    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public String showRegistrationForm() {
        return "customerRegistration";
    }

    @PostMapping
    public String registerCustomer(@RequestParam String name, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String address,
                                   @RequestParam String phone, HttpSession session, Model model) {

        boolean isRegistered = customerRepository.registerCustomer(name, email, password, address, phone);

        if (isRegistered) {
            session.setAttribute("name", name);
            session.setAttribute("email", email);
            session.setAttribute("address", address);
            session.setAttribute("phone", phone);
            return "redirect:/customerLogin";
        } else {
            model.addAttribute("error", true);
            return "customerRegistration";
        }
    }
}
