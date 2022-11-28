package com.revature.ticketer.handlers;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidUserException;
import com.revature.ticketer.dtos.requests.NewUserRequest;
import com.revature.ticketer.services.UserService;

import io.javalin.http.Context;

/*
 * Userhandler class will handle http verbs and endpoints
 */
public class UserHandler {

    private final UserService userService;
    private final ObjectMapper mapper;
     
    public UserHandler(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public void signup(Context c) throws IOException{
        //Returns the JSON request and maps it to NewUserRequest
        NewUserRequest req = mapper.readValue(c.req.getInputStream(), NewUserRequest.class);
        try{
            userService.saveUser(req);
            c.status(201); //Created Status Code. Implies that a new user has been created
        } catch (InvalidUserException e){
            c.status(409); //Conflict Status Code, meaning the user already exists
            //Maybe have a child class for this exception so it is a little bit more specific and useful for logging
            c.json(e);
        }
    }
}
