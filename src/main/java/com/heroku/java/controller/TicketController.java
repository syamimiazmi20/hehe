package com.heroku.java.controller;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.heroku.java.model.ticket;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.Map;

import java.util.List;

@Controller
public class TicketController {
    private final DataSource dataSource;

    @Autowired
    public TicketController(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @PostMapping("/addTickets")
    public String addTicket(@ModelAttribute("addTickets") ticket tickets) {
        try {
            System.out.println("Ticket Type: " + tickets.getTicketType());
            System.out.println("Ticket Price: " + tickets.getTicketPrice());
    
            try (Connection connection = dataSource.getConnection()) {
                String sql = "INSERT INTO public.ticket(tickettype,ticketprice) VALUES(?,?)";
                final var statement = connection.prepareStatement(sql);
    
                String tickettype = tickets.getTicketType();
                double ticketprice = tickets.getTicketPrice();
    
                statement.setString(1, tickettype);
                statement.setDouble(2, ticketprice);
    
                statement.executeUpdate();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    
        return "redirect:/index";
    }

    

    @GetMapping("/ticketList")
    public String ticketList(Model model){
    List<ticket> tickets = new ArrayList<ticket>();

    try(Connection connection= dataSource.getConnection()){
    String sql = "Select ticketid, tickettype,ticketprice From public.ticket order by ticketid";
    final var statement= connection.prepareStatement(sql);
    final var resultSet = statement.executeQuery();

    while (resultSet.next()){
        int ticketID= resultSet.getInt("ticketID");
        String ticketType= resultSet.getString("ticketType");
        double ticketPrice = resultSet.getDouble("ticketPrice");

        ticket ticket = new ticket();
        ticket.setTicketId(ticketID);
        ticket.setTicketType(ticketType);
        ticket.setTicketPrice(ticketPrice);

        tickets.add(ticket);
        model.addAttribute("tickets",tickets);
    }

    connection.close();

    return "Ticket/bookingTicket";

    } catch (SQLException e){
        e.printStackTrace();

        return "error";
    }
}
}
