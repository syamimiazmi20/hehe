package com.heroku.java.controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.ticket;

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
    public String ticketList(Model model) {
        List<ticket> tickets = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT  ticketid, tickettype, ticketprice FROM public.ticket";
            final var statement = connection.prepareStatement(sql);
            final var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long ticketId = resultSet.getLong("ticketid");
                String ticketType = resultSet.getString("tickettype");
                double ticketPrice = resultSet.getDouble("ticketprice");

                ticket ticket = new ticket();
                ticket.setTicketId(ticketId);
                ticket.setTicketType(ticketType);
                ticket.setTicketPrice(ticketPrice);

                tickets.add(ticket);
            }

            model.addAttribute("tickets", tickets);

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        return "Ticket/bookingTicket";
    }


@GetMapping("/updateTicket")
public String updateTicket(@RequestParam("ticketid") Long ticketID, Model model) {

    try {
        Connection connection = dataSource.getConnection();
        String sql = "SELECT ticketid,tickettype,ticketprice FROM public.ticket WHERE ticketid=?";
        final var statement= connection.prepareStatement(sql);
        statement.setLong(1,ticketID);
        final var resultSet = statement.executeQuery();

        if (resultSet.next()){
            
            String ticketType = resultSet.getString("tickettype");
            double ticketPrice = resultSet.getDouble("ticketprice");

            ticket ticket = new ticket();
            ticket.setTicketId(ticketID);
            ticket.setTicketPrice(ticketPrice);
            ticket.setTicketType(ticketType);

            connection.close();
        }
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return "Ticket/updateTicket";
}

@PostMapping("/updateTicket")
public String updateTicket(@ModelAttribute("updateTicket") ticket ticket, Model model){
 
    try {
        Connection connection = dataSource.getConnection();
        String sql = "UPDATE ticket SET tickettype=? , ticketprice=? WHERE ticketid=?";
        final var statement=connection.prepareStatement(sql);
        String ticketType = ticket.getTicketType();
        double ticketPrice = ticket.getTicketPrice();

        statement.setString(1,ticketType);
        statement.setDouble(2,ticketPrice);

        statement.executeUpdate();

        connection.close();
        

    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/error";
    }
    return "redirect:/bookingTicket";
}
}