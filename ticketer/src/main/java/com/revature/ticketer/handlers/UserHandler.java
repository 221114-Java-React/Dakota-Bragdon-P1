package com.revature.ticketer.handlers;

import com.revature.ticketer.TicketStatusService.UserService;

import io.javalin.http.Context;

/*
 * Userhandler class will handle http verbs and endpoints
 */
public class UserHandler {

    private final UserService userService;
     
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void signup(Context c){

    }
}
