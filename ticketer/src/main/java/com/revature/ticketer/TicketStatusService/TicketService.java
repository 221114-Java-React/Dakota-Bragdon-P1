package com.revature.ticketer.TicketStatusService;

import com.revature.ticketer.daos.TicketDAO;

public class TicketService {
    private final TicketDAO ticketDAO;

    public TicketService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }
}
