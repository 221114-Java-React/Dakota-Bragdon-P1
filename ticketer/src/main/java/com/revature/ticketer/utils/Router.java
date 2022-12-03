package com.revature.ticketer.utils;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.daos.TicketDAO;
import com.revature.ticketer.daos.UserDAO;
import com.revature.ticketer.handlers.AuthHandler;
import com.revature.ticketer.handlers.TicketHandler;
import com.revature.ticketer.handlers.UserHandler;
import com.revature.ticketer.services.TicketService;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.services.UserService;

import io.javalin.Javalin;

/*
 * The purpose of the Router is to map endpoints.
 */
public class Router {

    public static void router(Javalin app){
        ObjectMapper mapper = new ObjectMapper();
        JwtConfig jwtConfig = new JwtConfig();
        TokenService tokenService = new TokenService(jwtConfig);

        /*
         * Hierarchy of dependency injection
         * UserDAO > UserService > UserHandler
         */
        //Users
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);
        UserHandler userHandler = new UserHandler(userService, mapper, tokenService);

        //Auth
        AuthHandler authHandler = new AuthHandler(userService, mapper, tokenService);

        //Tickets
        TicketDAO ticketDAO = new TicketDAO();
        TicketService ticketService = new TicketService(ticketDAO);
        TicketHandler ticketHandler = new TicketHandler(ticketService, mapper, tokenService);

        //User Handler Group
        //Goes Routes, userHandler, userService, userDAO
        //A POST request would be an attempt at making a new user in this path. This is why userHandler's
        //signup is called on c, which would be the passed data
        app.routes(() -> {
            //User
            path("/users", () -> {
                //Post takes in a context which points to a function body
                //Turns a function into a variable
                post(c -> userHandler.signup(c)); //Signs up a user as either an Employee or Manager

                get(c -> userHandler.getAllUsers(c)); //This can only be performed by admin
                
                get("/name", c -> userHandler.getAllUsersByUsername(c)); 

                path("/manageUsers", () -> { //THIS WILL REQUIRE ADMINISTRATIVE PRIVILEDGES
                    
                    //get(c -> userHandler.getPendingUsers(c)); //Will return a list of users who aren't validated yet
                    
                    //put("/name", c -> userHandler.setPassword(c));
                    
                    //Try patching/putting the password
                });

            });

            //Auth
            path("/auth", () ->{
                post(c -> authHandler.authenticateUser(c)); //Used to log the user in
                path("/validate", () ->{
                    patch("/name", c -> authHandler.validateUser(c)); //Will validate a user
                });
                //patch( c-> authHandler.setUserPassword(c)); //Will change a specific user's password
                
            });

            //Ticket
            path("/ticket", () -> {
                get(c -> ticketHandler.getAllTickets(c)); //Returns all tickets
                post(c -> ticketHandler.makeTicket(c));//Used to make tickets
                
                //Path to an individual employee's tickets
                path("/user", () -> {
                    get(c -> ticketHandler.getEmployeeTickets(c)); //Returns all of an employee's tickets
                    //patch(c -> ticketHandler.updateEmployeeTicket(c)); //Allows an employee to update a ticket

                });

                //Handles pending tickets
                path("pending", () -> {
                    get(c -> ticketHandler.getPendingTickets(c));//Gets a list of all pending tickets
                    patch("/id", c -> ticketHandler.resolveTicket(c)); //Resolves a pending ticket to be either approved or denied
                });

                path("/resolved_list", () -> {
                    //get(c -> ticketHandler.getResolvedList()); //Returns a list of tickets a manager has resolved
                });
            });

        });
    }
}
