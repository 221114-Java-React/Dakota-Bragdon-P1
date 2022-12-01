package com.revature.ticketer.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.dtos.requests.NewTicketRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.models.Ticket;
import com.revature.ticketer.services.TicketService;
import com.revature.ticketer.services.TokenService;

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
            if(!isValidToken(token)) throw new InvalidAuthException("ERROR: You lack authorization");

            ticketService.saveTicket(req);//Adds a ticket
            c.status(201);

        } catch (InvalidAuthException e) {
            c.status(401);
            c.json(e);
        }
    }

    public void getAllTickets(Context c) throws IOException{
        
    }

    //Checks to make sure the token generated is valid
    //Currently only claims users are valid
    public boolean isValidToken(String token){

        if(token == null || token.isEmpty()) throw new InvalidAuthException("ERROR: You are not signed in");
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) throw new InvalidAuthException("ERROR: Invalid Token");
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f3")) throw new InvalidAuthException("ERROR: You lack authorization to do this");

        return true;
    }

}
