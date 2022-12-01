package com.revature.ticketer.utils;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;

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

                get(c -> userHandler.getAllUsers(c)); //This can only be performed by an admin
                
                get("/name", c -> userHandler.getAllUsersByUsername(c));
                //get("/role", c -> userHandler.getAllUsersByRole(c));//Will return a list of users ordered by role
                //delete(c - userHandler.removeUser(c)); Will remove a user based on an id
                path("/manageUsers", () -> { //THIS WILL REQUIRE ADMINISTRATIVE PRIVILEDGES
                    
                    //get(c -> userHandler.getPendingUsers(c)); //Will return a list of users who aren't validated yet
                    patch("/name", c -> userHandler.validateUser(c)); //Will validate a user

                    //Try patching/putting the password
                });

            });

            //Auth
            path("/auth", () ->{
                post(c -> authHandler.authenticateUser(c)); //Used to log the user in
                //authHandler::authenticateUser This is an example of method reference and automatically passes the context
                //delete(c -> userHandler.invalidateUser(c)) //Invalidates a user (sets is_Active to false) [CHECK IF THIS ISN'T ALREADY THE CASE]
                //put(c -> userHandler.validateUser(c)) //Validates a user (sets is_Active to true) [CHECK IF THIS ISN'T ALREADY THE CASE]
            });

            //Ticket
            path("/ticket", () -> {
                get(c -> ticketHandler.getAllTickets(c)); //Returns all tickets
                post(c -> ticketHandler.makeTicket(c));//Used to make tickets
                patch("/id", c -> ticketHandler.resolveTicket(c)); //Resolves a pending ticket to be either approved or denied

                //get("/user" -> ticketHandler.getUserTickets);//Returns all tickets for a user

                path("pending", () -> {
                    get(c -> ticketHandler.getPendingTickets(c));//Gets a list of all pending tickets
                    //Maybe get a list for a specific user?
                });
            });

        });
    }
}
