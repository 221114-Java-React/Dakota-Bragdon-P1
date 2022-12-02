package com.revature.ticketer.models;

import java.sql.Timestamp;

import javax.sql.rowset.serial.SerialBlob;

/*
 * Contains all necessary ticket information
 */
public class Ticket {
    private String id;
    private double amount;
    private Timestamp sumbittedTime;
    private Timestamp resolveTime;
    private String description;
    private SerialBlob receipt;
    private String paymentId;
    private String authorId;
    private String resolverId;
    private String status;
    private String type;
    
    public Ticket(){
        super();
    }

    public Ticket(String id, double amount, Timestamp sumbittedTime, Timestamp resolveTime, String description,
            String paymentId, String authorId, String resolverId, String status, String type, SerialBlob receipt) {
        this.id = id;
        this.amount = amount;
        this.sumbittedTime = sumbittedTime;
        this.resolveTime = resolveTime;
        this.description = description;
        this.receipt = receipt;
        this.paymentId = paymentId;
        this.authorId = authorId;
        this.resolverId = resolverId;
        this.status = status;
        this.type = type;
    }

    


    public Ticket(String id, Timestamp resolveTime, String resolverId, String status) {
        this.id = id;
        this.resolveTime = resolveTime;
        this.resolverId = resolverId;
        this.status = status;
    }

    public Ticket(String id, double amount, Timestamp sumbittedTime, Timestamp resolveTime, String description,
            String paymentId, String authorId, String resolverId, String status, String type) {
        this.id = id;
        this.amount = amount;
        this.sumbittedTime = sumbittedTime;
        this.resolveTime = resolveTime;
        this.description = description;
        this.paymentId = paymentId;
        this.authorId = authorId;
        this.resolverId = resolverId;
        this.status = status;
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

    public Timestamp getSumbittedTime() {
        return sumbittedTime;
    }

    public void setSumbittedTime(Timestamp sumbittedTime) {
        this.sumbittedTime = sumbittedTime;
    }

    public Timestamp getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(Timestamp resolveTime) {
        this.resolveTime = resolveTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SerialBlob getReceipt() {
        return receipt;
    }

    public void setReceipt(SerialBlob receipt) {
        this.receipt = receipt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getResolverId() {
        return resolverId;
    }

    public void setResolverId(String resolverId) {
        this.resolverId = resolverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", amount=" + amount + ", sumbittedTime=" + sumbittedTime + ", resolveTime="
                + resolveTime + ", description=" + description + ", receipt=" + receipt + ", paymentId=" + paymentId
                + ", authorId=" + authorId + ", resolverId=" + resolverId + ", status=" + status + ", type=" + type
                + "]";
    }
    
}
