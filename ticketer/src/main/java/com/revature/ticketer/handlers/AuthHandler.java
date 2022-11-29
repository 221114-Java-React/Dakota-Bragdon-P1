package com.revature.ticketer.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.dtos.requests.NewLoginRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.models.User;
import com.revature.ticketer.services.UserService;

import io.javalin.http.Context;

/*
 * Used to authenticate if the request made is valid and from
 * a certain type of user. Will grant that user
 * certain permissions depending on their authentication level
 */
public class AuthHandler {
    private final UserService userService;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(User.class); //Logger is used to display information while debugging


    public AuthHandler(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public void authenticateUser(Context c) throws IOException {
        NewLoginRequest req = mapper.readValue(c.req.getInputStream(), NewLoginRequest.class);
        //logger.info(req.toString()); Logs the New Login Request
        logger.info("Attempting to log in...");
        try{
            Principal principal = userService.login(req);
            logger.info("Log in successful");
        } catch (InvalidAuthException e){
            c.status(401); //Invalid Authorization status code
            c.json(e);
        }
    }

    
}
