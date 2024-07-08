package com.heroku.java.repository;

import com.heroku.java.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByEmail(String email);

    Customer findByEmailAndPassword(String email, String password);

    List<Customer> findByNameContaining(String name);

    @Query("SELECT c FROM Customer c WHERE c.address LIKE %:keyword%")
    List<Customer> findByAddressContaining(@Param("keyword") String keyword);

    @Query("SELECT c FROM Customer c WHERE c.phoneNum = :phoneNum")
    Customer findByPhoneNum(@Param("phoneNum") String phoneNum);

    @Query(value = "SELECT * FROM customers WHERE YEAR(created_at) = :year", nativeQuery = true)
    List<Customer> findCustomersRegisteredInYear(@Param("year") int year);

    boolean existsByEmail(String email);

    long countByAddress(String address);

    @Query("SELECT c FROM Customer c ORDER BY c.name ASC")
    List<Customer> findAllOrderedByName();

    @Query("DELETE FROM Customer c WHERE c.email = :email")
    void deleteByEmail(@Param("email") String email);

    Customer save(Customer customer);
}