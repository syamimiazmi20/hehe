package com.heroku.java.repository;

import com.heroku.java.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.password = :password")
    Customer authenticateCustomer(@Param("email") String email, @Param("password") String password);
}
