package com.revature.ticketer.dtos.requests;

public class NewTicketRequest {
    private String id;
    private double amount;
    private String description;
    private String type;
    private String status;

    public NewTicketRequest(){
        super();
    }  

    public NewTicketRequest(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public NewTicketRequest( double amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    //Used by managers to resolve a ticket
    public NewTicketRequest(String status) {
        this.status = status;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NewTicketRequest [id=" + id + ", amount=" + amount + ", description=" + description + ", type=" + type
                + ", status=" + status + "]";
    }
    
}
