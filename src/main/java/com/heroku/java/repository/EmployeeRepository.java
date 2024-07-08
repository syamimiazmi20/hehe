package com.heroku.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.heroku.java.model.Employee;;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmailAndPassword(String email, String password);

    List<Employee> findAll();

    long count();

    // Custom query methods
    List<Employee> findByRole(String role);

    @Query("SELECT e FROM Employee e WHERE e.name LIKE %:keyword% OR e.email LIKE %:keyword%")
    List<Employee> searchEmployees(@Param("keyword") String keyword);

    @Query("SELECT e FROM Employee e WHERE e.adminId = :adminId")
    List<Employee> findEmployeesByAdminId(@Param("adminId") int adminId);

    boolean existsByEmail(String email);

    @Query("UPDATE Employee e SET e.password = :newPassword WHERE e.id = :employeeId")
    int updatePassword(@Param("employeeId") int employeeId, @Param("newPassword") String newPassword);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.role = :role")
    long countByRole(@Param("role") String role);
}