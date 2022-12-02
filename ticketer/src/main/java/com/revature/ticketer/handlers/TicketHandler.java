package com.revature.ticketer.handlers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.dtos.requests.NewTicketRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.models.Ticket;
import com.revature.ticketer.services.TicketService;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.utils.CheckToken;

import io.javalin.http.Context;

public class TicketHandler {

    private final TicketService ticketService;
    private final ObjectMapper mapper;
    private final TokenService tokenService;
    private final static Logger logger = LoggerFactory.getLogger(Ticket.class);
    
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
            CheckToken.isValidEmployeeToken(token, tokenService);

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
            CheckToken.isValidManagerToken(token, tokenService);
            List<Ticket> tickets = ticketService.getAllTickets();
            c.json(tickets);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

    public void getPendingTickets(Context c) throws IOException{
        try{
            String token = c.req.getHeader("authorization");
            CheckToken.isValidManagerToken(token, tokenService);
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
            CheckToken.isValidManagerToken(token, tokenService);
            String resolverId = CheckToken.getOwner(token, tokenService);
            ticketService.resolveTicket(req, ticketId, resolverId);
            c.status(202);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

}
