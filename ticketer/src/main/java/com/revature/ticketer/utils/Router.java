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
        TicketHandler ticketHandler = new TicketHandler(ticketService);

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
                post(c -> userHandler.signup(c));

            });

            //Auth
            path("/auth", () ->{
                post(c -> authHandler.authenticateUser(c));
                //authHandler::authenticateUser This is an example of method reference and automatically passes the context
            });
        });
    }
}
