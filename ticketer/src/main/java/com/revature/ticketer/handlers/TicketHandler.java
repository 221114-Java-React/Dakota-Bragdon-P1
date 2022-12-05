package com.revature.ticketer.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidActionException;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.Exceptions.InvalidInputException;
import com.revature.ticketer.Exceptions.NotFoundException;
import com.revature.ticketer.dtos.requests.NewTicketRequest;
import com.revature.ticketer.models.Ticket;
import com.revature.ticketer.services.TicketService;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.services.UserService;
import com.revature.ticketer.utils.CheckToken;

import io.javalin.http.Context;

public class TicketHandler {

    private final TicketService ticketService;
    private final UserService userService; //Used to check if a user is in the database when looking for a user's tickets
    private final ObjectMapper mapper;
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(TicketHandler.class);
    
    public TicketHandler(TicketService ticketService, UserService userService, ObjectMapper mapper,
            TokenService tokenService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    //Saves a new ticket to the database
    public void makeTicket(Context c) throws IOException{ 
        Ticket ticket = new Ticket();
        try{
            NewTicketRequest req = mapper.readValue(c.req.getInputStream(), NewTicketRequest.class);
            String token = c.req.getHeader("authorization");
            if(!ticketService.isValidAmount(req.getAmount())) throw new InvalidInputException("ERROR: Amount cannot be less than 0");
            if(CheckToken.isValidEmployeeToken(token, tokenService)) {
                String ownerId = CheckToken.getOwner(token, tokenService);
                ticket = ticketService.saveTicket(req, ownerId);//Adds a ticket
            } else throw new InvalidAuthException("Only employees can make new tickets");
            
            logger.info("Successfully submitted a new ticket");
            c.json(ticket);
            c.status(201);        
        } catch (InvalidAuthException e) {
            c.status(401);
            c.json(e);
        } catch (JsonParseException e) {
            logger.info("Malformed Input");
            c.status(400);
            c.json(e);
        } catch (InvalidInputException e){
            c.status(400);
            c.json(e);
        }
    }

    //Checks if the ticket exists before updating it
    public void updateTicket(Context c) throws IOException{
        Ticket updatedTicket = new Ticket();
        try{
            NewTicketRequest req = mapper.readValue(c.req.getInputStream(), NewTicketRequest.class);
            String token = c.req.getHeader("authorization");
            String ticketId = c.req.getParameter("id");
            //Checks if token is an employee token. Then checks the fields. If any are null, they will not be updated
            //(Type is updated in ticketService)
            if(CheckToken.isValidEmployeeToken(token, tokenService)) {
                if(!ticketService.isValidAmount(req.getAmount())) throw new InvalidInputException("ERROR: Amount cannot be less than 0");
                Ticket ticket = ticketService.getTicket(ticketId);
                if(ticket != null) {
                    if(!ticket.getStatus().equals("b0ccfca2-6f8e-11ed-a1eb-0242ac120002")) throw new InvalidActionException("ERROR: Ticket has already been finalized");
                    if(req.getDescription() != null) ticket.setDescription(req.getDescription());
                    if (req.getAmount() != 0) ticket.setAmount(req.getAmount());
                    String type = req.getType();
                    updatedTicket = ticketService.updateTicket(ticket, type);
                } else throw new NotFoundException("ERROR: No ticket was found");
            } else throw new InvalidAuthException("Only employees update their tickets (EXCLUDING THE STATUS)");

            logger.info("Successfully updated the ticket");
            c.json(updatedTicket);
            c.status(201);  
        } catch (InvalidAuthException e) {
            c.status(401);
            c.json(e);
        } catch (JsonParseException e) {
            logger.info("Malformed Input");
            c.status(400);
            c.json(e);
        }  catch (InvalidInputException e){
            c.status(400);
            c.json(e);
        } catch (InvalidActionException e){
            c.status(403);
            c.json(e);
        }
    }

    //Resolves a ticket
    public void resolveTicket(Context c) throws IOException{
        try{
            NewTicketRequest req = mapper.readValue(c.req.getInputStream(), NewTicketRequest.class);
            String token = c.req.getHeader("authorization");
            String ticketId = c.req.getParameter("id");
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only managers can resolve tickets");
            String resolverId = CheckToken.getOwner(token, tokenService);

            Ticket ticket = ticketService.getTicket(ticketId);
            if(ticket == null) throw new NotFoundException("ERROR: No ticket was found");
            //Hardcoded this. Not the best practice. IF TIME ALLOWS COME BACK TO RESOLVE THIS
            if(!ticket.getStatus().equals("b0ccfca2-6f8e-11ed-a1eb-0242ac120002")) throw new InvalidActionException("ERROR: Ticket has already been finalized");
            ticketService.resolveTicket(req, ticketId, resolverId);
            logger.info("Successfully resolved a ticket");
            c.status(202);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (InvalidActionException e){
            c.status(403);
            c.json(e);
        } catch (JsonParseException e) {
            logger.info("Malformed Input");
            c.status(400);
            c.json(e);
        } catch (NotFoundException e) {
            c.status(404);
            c.json(e);
        }
    }

    /*
     * Returns a list of all the tickets for a SINGLE employee. Employees will only be able to view their own while a manager will be able
     * to view a specific employee's tickets
     */
    public void getEmployeeTickets(Context c) throws IOException{

        try{
            List<Ticket> tickets = new ArrayList<>();
            String token = c.req.getHeader("authorization");
            String searcherUsername = CheckToken.getOwnerUsername(token, tokenService); //Gets the current user's ID, will be used for checking whether an employee is looking at their own ticket(s)
            String targetUsername = c.req.getParameter("username"); //Gets the target's ID
   

            //Checks to make sure the token owner is other an employee or manager. This will determine the serviceHandler method called
            if(CheckToken.isValidManagerToken(token, tokenService) || CheckToken.isValidEmployeeToken(token, tokenService)) {
                if(searcherUsername.equals(targetUsername)){//This means the employee is searching for their own tickets
                    tickets = ticketService.getAllUserTickets(CheckToken.getOwner(token, tokenService)); //Returns the tickets for a specific user
                } else { //Means a manager is searching for other tickets

                    String id = userService.getIdfromUsername(targetUsername);
                    if (!id.equals("")) {
                        if(CheckToken.isValidManagerToken(token, tokenService)) {
                            tickets = ticketService.getAllUserTickets(id); //Returns the tickets for all users
                        } else throw new InvalidAuthException("ERROR: Only managers can view other user's tickets");
                    } else throw new NotFoundException("ERROR: Target username is not in the database");  
                }

                if(tickets.isEmpty()) throw new NotFoundException("ERROR: No ticket(s) were found");
                c.json(tickets);
            } else throw new InvalidAuthException("ERROR: Only Employees and Managers can view tickets");

            c.status(200);
        }  catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (InvalidActionException e){
            c.status(403);
            c.json(e);
        } catch (NotFoundException e) {
            logger.info("No tickets found");
            c.status(404);
            c.json(e);
        }
    }

     //Returns a list of all pending tickets
    public void getPendingTickets(Context c) throws IOException{
        try{
            String token = c.req.getHeader("authorization");
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only managers can view all pending tickets");
            List<Ticket> tickets = ticketService.getAllPendingTickets(); //Returns the all the pending tickets for all users
            if(tickets.isEmpty()) throw new NotFoundException("ERROR: No ticket(s) were found");
            c.json(tickets);
            c.status(200);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (NotFoundException e){
            logger.info("No tickets found");
            c.status(404);
            c.json(e);
        }
    }

    public void getResolvedList(Context c) throws IOException{
        try{
            String token = c.req.getHeader("authorization");
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only managers can view all the tickets they have resolved");
            List<Ticket> tickets = ticketService.getResolvedTickets(CheckToken.getOwner(token, tokenService));
            if(tickets.isEmpty()) throw new NotFoundException("ERROR: This manager has not resolved any tickets");
            c.json(tickets);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (NotFoundException e) {
            logger.info("No tickets found");
            c.status(404);
            c.json(e);
        }
    }

    //Shows all the tickets in the database
    public void getAllTickets(Context c) throws IOException{
        try{
            String token = c.req.getHeader("authorization");
            if(!CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("Only managers can view all tickets");
            List<Ticket> tickets = ticketService.getAllTickets();
            if(tickets.isEmpty()) throw new NotFoundException("ERROR: No ticket(s) were found");
            c.json(tickets);
            c.status(200);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (NotFoundException e){
            logger.info("No tickets found");
            c.status(404);
            c.json(e);
        }
    }
}
