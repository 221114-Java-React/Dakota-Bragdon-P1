package com.revature.ticketer.handlers;

import java.io.IOException;
import java.util.List;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.Exceptions.InvalidUserException;
import com.revature.ticketer.Exceptions.NotFoundException;
import com.revature.ticketer.dtos.requests.NewUserRequest;
import com.revature.ticketer.models.User;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.services.UserService;
import com.revature.ticketer.utils.CheckToken;

import io.javalin.http.Context;

/*
 * Userhandler class will handle http verbs and endpoints
 */
public class UserHandler {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final TokenService tokenService;
     


    public UserHandler(UserService userService, ObjectMapper mapper, TokenService tokenService) {
        this.userService = userService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    //Signs up a new user
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

    //Returns all users
    public void getAllUsers(Context c){
        try{ 
            String token = c.req.getHeader("authorization");//Generates String token from the Header of the Post Request
            CheckToken.isValidAdminToken(token, tokenService);
            List<User> users = userService.getAllUsers();
            c.json(users);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        }
    }

    public void getAllUsersByUsername(Context c){
        try{
            //Gets the username from the URI
            String username = c.req.getParameter("username");
            String token = c.req.getHeader("authorization"); 
            if(CheckToken.isValidAdminToken(token, tokenService) && CheckToken.isValidManagerToken(token, tokenService)) throw new InvalidAuthException("ERROR: Invalid token");
            List<User> users = userService.getAllUsersByUsername(username);
            if(users.isEmpty()) throw new NotFoundException("ERROR: No user(s) were found");
            c.status(200);
            c.json(users);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (NotFoundException e){
            c.status(404);
            c.json(e);
        }
    }

    //Validates the user
    //CONSIDER ADDING A CHECK TO MAKE SURE THE USER EXISTS IN THE DATABASE!
    /*public void validateUser(Context c) throws IOException{
        NewUserRequest req = mapper.readValue(c.req.getInputStream(), NewUserRequest.class);
        try {
            String username = c.req.getParameter("username");
            String token = c.req.getHeader("authorization"); 
            CheckToken.isValidAdminToken(token, tokenService);
            if(!CheckToken.isValidAdminToken(token, tokenService)) throw new InvalidAuthException("ERROR: Invalid token");
            userService.validateUser(req, username);
        } catch (InvalidAuthException e){
            e.printStackTrace();
        }
    }*/
}
