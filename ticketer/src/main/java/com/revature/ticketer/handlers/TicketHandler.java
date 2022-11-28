package com.revature.ticketer.handlers;

import com.revature.ticketer.services.TicketService;

public class TicketHandler {
    
    private final TicketService ticketService;
     
    public TicketHandler(TicketService ticketService) {
        this.ticketService = ticketService;
    }

}
