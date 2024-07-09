package com.heroku.java.repository;

import com.heroku.java.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

    Customer findByEmail(String email);
}

interface CustomerRepositoryCustom {
    boolean registerCustomer(String name, String email, String password, String address, String phone);

    boolean authenticateCustomer(String email, String password);
}

class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    private static final String URL = "jdbc:your_database_url";
    private static final String USER = "your_database_user";
    private static final String PASSWORD = "your_database_password";

    @Override

    public boolean registerCustomer(String name, String email, String password, String address, String phone) {
        boolean isSuccess = false;
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "INSERT INTO CUSTOMERS (cust_name, cust_email, cust_password, cust_address, cust_phoneNum) VALUES (?, ?, ?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, address);
            ps.setString(5, phone);

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(con, ps, null);
        }

        return isSuccess;
    }

    @Override

    public boolean authenticateCustomer(String email, String password) {
        boolean isAuthenticated = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "SELECT * FROM CUSTOMERS WHERE cust_email = ? AND cust_password = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                isAuthenticated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(con, ps, rs);
        }

        return isAuthenticated;
    }

    private void closeResources(Connection con, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
