package com.revature.ticketer.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.Exceptions.InvalidInputException;
import com.revature.ticketer.dtos.requests.NewLoginRequest;
import com.revature.ticketer.dtos.requests.UpdateUserRequest;
import com.revature.ticketer.dtos.requests.ValidateNewUserRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.services.UserService;
import com.revature.ticketer.utils.CheckToken;

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
    private static final Logger logger = LoggerFactory.getLogger(AuthHandler.class); //Logger is used to display information while debugging

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

    //Validates the user
    //CONSIDER ADDING A CHECK TO MAKE SURE THE USER EXISTS IN THE DATABASE!
    public void validateUser(Context c) throws IOException{
        ValidateNewUserRequest req = mapper.readValue(c.req.getInputStream(), ValidateNewUserRequest.class);
        try {
            String username = c.req.getParameter("username");
            String token = c.req.getHeader("authorization"); 
            if(!CheckToken.isValidAdminToken(token, tokenService)) throw new InvalidAuthException("ERROR: Invalid token");
            userService.validateUser(req, username);
            logger.info("Validation Successful");
            c.status(202);
        } catch (InvalidAuthException e){
            e.printStackTrace();
        }
    }

    //Allows for an admin to change a user's password
    public void setUserPassword(Context c) throws IOException{
        UpdateUserRequest req = mapper.readValue(c.req.getInputStream(), UpdateUserRequest.class);
        try {
            String username = c.req.getParameter("username");
            String token = c.req.getHeader("authorization");
            if(!CheckToken.isValidAdminToken(token, tokenService)) throw new InvalidAuthException("ERROR: Invalid token");
            userService.updateUserPassword(username, req);
            logger.info("Password Change Successful");
            c.status(202);
        } catch (InvalidAuthException e){
            c.status(401);
            e.printStackTrace();
        } catch (InvalidInputException e){
            c.status(401);
            e.printStackTrace();
        }
    }
    
}
