package com.revature.ticketer;

import java.sql.SQLException;

import com.revature.ticketer.utils.Router;

import io.javalin.Javalin;

public class MainDriver {
    
    public static void main(String[] args) throws SQLException{

        //initializes a path to localhost:8080/ticketer
        Javalin app = Javalin.create(c -> {
            c.contextPath = "/ticketer";
        }).start(8080);

        Router.router(app);
    }
        
}
