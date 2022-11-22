package com.revature.ticketer.models;

import com.revature.ticketer.Exceptions.InvalidInputException;

/*
 * Contains all necessary ticket information
 * These amounts may only be read only after being submitted
 * So consider changing this in the future
 */
public class Ticket {
    private double amount;
    private String description;
    Status ticketStatus;

    enum Status{
        Pending,
        Rejected,
        Approved
    }

    private String type;//Convert this into type when
                        //the types are decided
    
    /*
     * Creates a Ticket.
     */
    public Ticket(int amount, String description, String type) throws InvalidInputException{
        if(amount <= 0 || description.equals("")) throw new InvalidInputException("ERROR: Invalid Input Detected");
        this.amount = amount;
        this.description = description;
        ticketStatus = Status.Pending;
        this.type = type;

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Status ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
}
