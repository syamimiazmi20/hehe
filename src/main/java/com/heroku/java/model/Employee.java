package com.heroku.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "EMPLOYEES")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int id;

    @Column(name = "emp_name")
    private String name;

    @Column(name = "emp_address")
    private String address;

    @Column(name = "emp_role")
    private String role;

    @Column(name = "emp_email", unique = true)
    private String email;

    @Column(name = "emp_password")
    private String password;

    @Column(name = "emp_phoneNum")
    private String phoneNum;

    @Column(name = "admin_id")
    private int adminId;

    // Default constructor
    public Employee() {}

    // Constructor with all fields
    public Employee(int id, String name, String address, String role, String email, String password, String phoneNum, int adminId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.role = role;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.adminId = adminId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}