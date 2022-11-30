package com.revature.ticketer.dtos.requests;

public class NewTicketRequest {
    private String id;
    private double amount;
    private String description;
    private String type;

    public NewTicketRequest(){
        super();
    }

    public NewTicketRequest(String id, double amount, String description, String type) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "NewTicketRequest [id=" + id + ", amount=" + amount + ", description=" + description + ", type=" + type
                + "]";
    }
    
}
