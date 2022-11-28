package com.revature.ticketer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.dtos.requests.NewLoginRequest;
import com.revature.ticketer.models.User;
import com.revature.ticketer.services.UserService;

import io.javalin.http.Context;

/*
 * Used to authenticate if the request made is valid and from
 * a certain type of user. Will grant that user
 * certain permissions depending on their authentication level
 */
public class AuthHandler {
    //Don't want these to change, so they are final
    private final UserService userService;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public AuthHandler(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public void authenticateUser(Context ctx){
        

    }

    
}
