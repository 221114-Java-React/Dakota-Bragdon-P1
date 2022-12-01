package com.revature.ticketer.utils;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;

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
                get(c -> userHandler.getAllUsers(c));
                get("/name", c -> userHandler.getAllUsersByUsername(c));
                //get("/role", c -> userHandler.getAllUsersByRole(c));//Will return a list of users ordered by role
                post(c -> userHandler.signup(c)); //THIS CURRENTLY AUTOMATICALLY ASSIGNS A USER AS AN EMPLOYEE. ADD AN ADDITIONAL JSON FIELD TO ALLOW FOR A USER TO INPUT
                                                    //EITHER EMPLOYEE OR MANAGER TO MEET THE NECESSARY REQUIREMENTS
                //delete(c - userHandler.removeUser(c)); Will remove a user based on an id
                path("/manageUsers", () -> { //THIS WILL REQUIRE ADMINISTRATIVE PRIVILEDGES
                    
                    //get(c -> userHandler.getPendingUsers(c)); //Will return a list of users who aren't validated yet
                    //post(c -> userHandler.validateUsers(c)) //

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
                post(c -> ticketHandler.makeTicket(c));//Used to make tickets
                //modify tickets
                //get("/user" -> ticketHandler.getUserTickets);//Returns an unordered list of tickets. Maybe add additional paths for sorting by type/amount/status (?)
                //get("/amount", c -> ticketHandler.) //Returns a sorted list of tickets
                //get("/")
            });

        });
    }
}
