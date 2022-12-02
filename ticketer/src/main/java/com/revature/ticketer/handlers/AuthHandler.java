package com.revature.ticketer.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.dtos.requests.NewLoginRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.models.User;
import com.revature.ticketer.services.TokenService;
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
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(User.class); //Logger is used to display information while debugging

    public AuthHandler(UserService userService, ObjectMapper mapper, TokenService tokenService) {
        this.userService = userService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    //Authenticates the user by generating a token from the principal
    public void authenticateUser(Context c) throws IOException {
        NewLoginRequest req = mapper.readValue(c.req.getInputStream(), NewLoginRequest.class);
        logger.info("Attempting to log in...");
        try{
            Principal principal = userService.login(req);

            //Attempts to generate a token
            String token = tokenService.generateToken(principal);

            //Sets header with an auth token
            c.res.setHeader("authorization", token);
            //Returns the principal as a json
            c.json(principal);

            logger.info("Log in successful");
            c.status(202);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

    
}
