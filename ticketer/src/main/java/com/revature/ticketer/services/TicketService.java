package com.revature.ticketer.services;

import java.sql.Timestamp;
import java.util.List;
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

        long now = System.currentTimeMillis();
        Timestamp makeTime = new Timestamp(now);
        //GET CURRENT USER ID USING TOKEN
        String typeId = ticketDAO.getTypeIdByType(request.getType());
        Ticket createdTicket = new Ticket(UUID.randomUUID().toString(), request.getAmount(), makeTime, null, 
            request.getDescription(), null, "1", null, "b0ccfca2-6f8e-11ed-a1eb-0242ac120002", typeId); //Set to PENDING status
        ticketDAO.save(createdTicket); //CHANGE THE AUTHOR ID
    }

    public void resolveTicket(NewTicketRequest request, String ticketId, String resolverId){
        long now = System.currentTimeMillis();
        Timestamp resolveTime = new Timestamp(now);
        String status = ticketDAO.getStatusIdByType(request.getStatus());
        Ticket resolvedTicket = new Ticket(ticketId, resolveTime, resolverId, status);
        ticketDAO.resolve(resolvedTicket);
    }

    public List<Ticket> getAllTickets(){
        return ticketDAO.findAllTickets();
    }

    public Ticket getTicket(String id){
        return ticketDAO.findById(id);
    }
}
