package com.heroku.java.model;

public class ticket {
    private String ticketType;
    private double ticketPrice;
    private Long ticketID;

    public ticket() {
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Long getTicketId() {
        return ticketID;
    }

    public void setTicketId(Long ticketID) {
        this.ticketID = ticketID;
    }
}
