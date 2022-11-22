package com.revature.ticketer;

import io.javalin.Javalin;
import com.revature.ticketer.utils.Router;

public class MainDriver {
    
    public static void main(String[] args){
        //initializes a path to localhost:8080/ticketer
        Javalin app = Javalin.create(c -> {
            c.contextPath = "/ticketer";
        }).start(8080);

        Router.router(app);
    }
}
