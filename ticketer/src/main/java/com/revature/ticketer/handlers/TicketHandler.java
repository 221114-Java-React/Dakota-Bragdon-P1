package com.revature.ticketer.handlers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.dtos.requests.NewTicketRequest;
import com.revature.ticketer.models.Ticket;
import com.revature.ticketer.services.TicketService;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.utils.CheckToken;

import io.javalin.http.Context;

public class TicketHandler {

    private final TicketService ticketService;
    private final ObjectMapper mapper;
    private final TokenService tokenService;
    
    public TicketHandler(TicketService ticketService, ObjectMapper mapper, TokenService tokenService) {
        this.ticketService = ticketService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public void makeTicket(Context c) throws IOException{

        NewTicketRequest req = mapper.readValue(c.req.getInputStream(), NewTicketRequest.class);
        try{
            //WILL NEED TO CHECK IF USER IS_ACTIVE BEFORE MAKING A TICKET
            String token = c.req.getHeader("authorization");
            //This message is vague, maybe modify it
            if(!CheckToken.isValidEmployeeToken(token, tokenService)) throw new InvalidAuthException("Only employees can make new tickets");

            ticketService.saveTicket(req);//Adds a ticket
            c.status(201);

        } catch (InvalidAuthException e) {
            c.status(401);
            c.json(e);
        }
    }

    //Mostly used for showing purposes
    public void getAllTickets(Context c) throws IOException{
        try{
            String token = c.req.getHeader("authorization");
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only managers can view all tickets");
            List<Ticket> tickets = ticketService.getAllTickets();
            c.json(tickets);
            c.status(200);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

    public void getPendingTickets(Context c) throws IOException{
        try{
            String token = c.req.getHeader("authorization");
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only managers can view all pending tickets");
            //List<Ticket> tickets = ticketService.getAllPendingTickets(); //Returns the all the pending tickets for all users
            //c.json(tickets);

            c.status(200);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

    public void resolveTicket(Context c) throws IOException{
        NewTicketRequest req = mapper.readValue(c.req.getInputStream(), NewTicketRequest.class);

        try{
            String token = c.req.getHeader("authorization");
            String ticketId = c.req.getParameter("id");
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only managers can resolve tickets");
            String resolverId = CheckToken.getOwner(token, tokenService);

            //try {
                Ticket ticket = ticketService.getTicket(ticketId);
                if(!ticket.getStatus().equals("b0ccfca2-6f8e-11ed-a1eb-0242ac120002")) throw new InvalidAuthException("ERROR: Ticket has been finalized");
                //Pretty scuffed right now. Will print out an Unauthorized Status code when it probably should be 400
            //} catch (InvalidAuthException e){ 
                //c.status(400);
                //c.json(e);
            //}
            ticketService.resolveTicket(req, ticketId, resolverId);
            c.status(202);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

    /*
     * Returns a list of all the tickets for an employee. Employees will only be able to view their own while a manager will be able
     * to view a specific employee's tickets
     */
    public void getEmployeeTickets(Context c) throws IOException{
        NewTicketRequest req = mapper.readValue(c.req.getInputStream(), NewTicketRequest.class);
        
        try{
            String token = c.req.getHeader("authorization");
            String searcherId = CheckToken.getOwner(token, tokenService); //Gets the current user's ID, will be used for checking whether an employee is looking at their own ticket(s)

            String targetId = c.req.getParameter("id"); //Gets the target's ID
            
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only Employees and Managers can view tickets");
            //THIS SHOULD THROW A 401 STATUS CODE

            if(searcherId.equals(targetId)){//This means the employee is searching for their own tickets
                List<Ticket> tickets = ticketService.getAllUserTickets(targetId); //Returns the tickets for a specific user
                c.json(tickets);
            } else {
                //validate to make sure the target's ID is in the database. Don't need to validate the current user since we already checked. DO THIS IN SERVICE
                //List<Ticket> tickets = ticketService.getAllUserTickets(targetId); //Returns the tickets for all users
                //c.json(tickets);
            }

            c.status(200);
        }  catch (InvalidAuthException e){
            c.status(404);
            c.json(e);
        }
    }
}
