package com.revature.ticketer.handlers;

import io.javalin.http.Context;
import com.revature.ticketer.services.UserService;

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
