package com.revature.ticketer.utils;

import com.revature.ticketer.handlers.UserHandler;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

/*
 * The purpose of the Router is to map endpoints.
 */
public class Router {

    public static void router(Javalin app){
        UserHandler userHandler = new UserHandler();

        //Handler Groups
        app.routes(() -> {
            path("/users", () -> {
                //Post takes in a context which points to a function body
                //Turns a function into a variable
                post(c -> userHandler.signup(c));
            });
        });
    }
}
