package com.revature.ticketer.handlers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.Exceptions.InvalidInputException;
import com.revature.ticketer.Exceptions.InvalidUserException;
import com.revature.ticketer.dtos.requests.NewUserRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.models.User;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.services.UserService;

import io.javalin.http.Context;

/*
 * Userhandler class will handle http verbs and endpoints
 */
public class UserHandler {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final TokenService tokenService;
    private final static Logger logger = LoggerFactory.getLogger(User.class);
     


    public UserHandler(UserService userService, ObjectMapper mapper, TokenService tokenService) {
        this.userService = userService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public void signup(Context c) throws IOException{
        //Returns the JSON request and maps it to NewUserRequest
        //This JSON request is the POST request made
        NewUserRequest req = mapper.readValue(c.req.getInputStream(), NewUserRequest.class);
        //Attempts to save the new user to the database
        try{
            userService.saveUser(req);
            c.status(201); //Created Status Code. Implies that a new user has been created
        } catch (InvalidUserException e){
            c.status(409); //Conflict Status Code, meaning the user already exists
            //Maybe have a child class for this exception so it is a little bit more specific and useful for logging
            c.json(e);
        }
    }

    public void getAllUsers(Context c){
        try{ 
            String token = c.req.getHeader("authorization");//Generates String token from the Header of the Post Request
            if(token == null || token.isEmpty()) throw new InvalidAuthException("ERROR: You are not signed in");
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("ERROR: Invalid Token");

            if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f5")) throw new InvalidAuthException("ERROR: You lack authorization to do this");
            List<User> users = userService.getAllUsers();
            c.json(users);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

    public void getAllUsersByUsername(Context c){
        try{
            String username = c.req.getParameter("username");
            String token = c.req.getHeader("authorization"); 
            if(token == null || token.isEmpty()) throw new InvalidAuthException("ERROR: You are not signed in");
            if(username == null || username.isEmpty()) throw new InvalidInputException("ERROR: Invalid Input");//Prevents possible issues with a blank username
            Principal principal = tokenService.extractRequesterDetails(token);
            if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f5")) throw new InvalidAuthException("ERROR: You lack authorization to do this");
            List<User> users = userService.getAllUsersByUsername(username);
            c.json(users);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }
}
