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

    //Passes a newly created ticket to the DAO
    public void saveTicket(NewTicketRequest request, String ownerId){

        long now = System.currentTimeMillis();
        Timestamp makeTime = new Timestamp(now);
        //GET CURRENT USER ID USING TOKEN
        String typeId = ticketDAO.getTypeIdByType(request.getType());
        Ticket createdTicket = new Ticket(UUID.randomUUID().toString(), request.getAmount(), makeTime, null, 
            request.getDescription(), null, ownerId, null, "b0ccfca2-6f8e-11ed-a1eb-0242ac120002", typeId); //Set to PENDING status
        ticketDAO.save(createdTicket);
    }

    public void resolveTicket(NewTicketRequest request, String ticketId, String resolverId){
        long now = System.currentTimeMillis();
        Timestamp resolveTime = new Timestamp(now);
        String status = ticketDAO.getStatusIdByStatus(request.getStatus());
        Ticket resolvedTicket = new Ticket(ticketId, resolveTime, resolverId, status);
        ticketDAO.resolve(resolvedTicket);
    }

    public List<Ticket> getAllTickets(){
        return ticketDAO.findAll();
    }

    //Given a user ID, returns a list containing all of that user's tickets
    public List<Ticket> getAllUserTickets(String id){
        return ticketDAO.findAllUserTickets(id);
    }

    public List<Ticket> getAllPendingTickets() {
        String status = ticketDAO.getStatusIdByStatus("PENDING");//This is passed in because it prevents me from hard coding it
                                                                            //Which is not good in case the id somehow changes
        return ticketDAO.findAllPendingTickets(status);
    }

    public List<Ticket> getResolvedTickets(String resolverId){
        return ticketDAO.findAllResolvedTickets(resolverId);
    }

    public Ticket getTicket(String id){
        return ticketDAO.findById(id);
    }

}
