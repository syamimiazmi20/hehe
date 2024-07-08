package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.heroku.java.model.Customer;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

    private final DataSource dataSource;

    @Autowired
    public CustomerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/customerRegister")
    public String customerRegister(@RequestParam("name") String name, @RequestParam("email") String email,
            @RequestParam("password") String password, @RequestParam("address") String address,
            @RequestParam("phone") String phone, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO customers (cust_name, cust_password, cust_email, cust_phone, cust_address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setString(5, address);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/createAccountSuccess";
    }

    @GetMapping("/customerLogin")
    public String customerLogin() {
        return "customerLogin";
    }

    @PostMapping("/customerLogins")
    public String customerLogin(HttpSession session, @RequestParam("email") String email,
            @RequestParam("password") String password, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, name, email, phone, address, password FROM customers WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String dbPassword = resultSet.getString("password");
                if (dbPassword.equals(password)) {
                    session.setAttribute("name", resultSet.getString("name"));
                    session.setAttribute("id", resultSet.getInt("id"));
                    return "redirect:/accLogin";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/customerLoginError";
    }

    @GetMapping("/customerProfile")
    public String customerProfile(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            return "redirect:/customerLogin";
        }
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT name, email, address, phone, password FROM customers WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setName(resultSet.getString("name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setAddress(resultSet.getString("address"));
                customer.setPhoneNum(resultSet.getString("phone"));
                customer.setPassword(resultSet.getString("password"));
                model.addAttribute("customer", customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "customerProfile";
    }

    @PostMapping("/customerUpdate")
    public String customerUpdate(HttpSession session, @ModelAttribute Customer customer) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            return "redirect:/customerLogin";
        }
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE customers SET name = ?, email = ?, address = ?, phone = ?, password = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getPhoneNum());
            statement.setString(5, customer.getPassword());
            statement.setInt(6, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/customerProfile";
    }

    @GetMapping("/customerDelete")
    public String customerDelete(HttpSession session) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            return "redirect:/customerLogin";
        }
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM customers WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/deleteAccountSuccess";
    }

    @GetMapping("/accLogin")
    public String accLogin(HttpSession session) {
        session.getAttribute("id");
        return "indexLogin";
    }

    @GetMapping("/createAccountSuccess")
    public String createAccountSuccess() {
        return "createAccountSuccess";
    }

    @GetMapping("/customerLoginError")
    public String customerLoginError() {
        return "customerLoginError";
    }

    @GetMapping("/deleteAccountSuccess")
    public String deleteAccountSuccess() {
        return "deleteAccountSuccess";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
