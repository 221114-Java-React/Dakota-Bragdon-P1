package com.revature.ticketer.services;

import java.sql.Timestamp;
import java.util.UUID;

import com.revature.ticketer.daos.TicketDAO;
import com.revature.ticketer.dtos.requests.NewTicketRequest;
import com.revature.ticketer.models.Ticket;

public class TicketService {
    private final TicketDAO ticketDAO;

    public TicketService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void saveTicket(NewTicketRequest request){
        //CHECK TO MAKE SURE TICKET IS VALID AND SET 1 TO BE THE TOKEN ID

        long now = System.currentTimeMillis();
        Timestamp makeTime = new Timestamp(now);
        //GET CURRENT USER ID USING TOKEN
        Ticket createdTicket = new Ticket(UUID.randomUUID().toString(), request.getAmount(), makeTime, null, 
        request.getDescription(), null, "1", null, "b0ccfca2-6f8e-11ed-a1eb-0242ac120002", request.getType());
        ticketDAO.save(createdTicket);
    }
}
