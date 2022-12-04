package com.revature.ticketer.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.Exceptions.InvalidInputException;
import com.revature.ticketer.Exceptions.NotFoundException;
import com.revature.ticketer.dtos.requests.NewLoginRequest;
import com.revature.ticketer.dtos.requests.UpdateUserRequest;
import com.revature.ticketer.dtos.requests.ValidateNewUserRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.models.User;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.services.UserService;
import com.revature.ticketer.utils.CheckToken;
import com.revature.ticketer.utils.HashString;

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

            String hashedPassword = HashString.hashString(req.getPassword());
            req.setPassword(hashedPassword);
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
        try {
            ValidateNewUserRequest req = mapper.readValue(c.req.getInputStream(), ValidateNewUserRequest.class);
            String username = c.req.getParameter("username");
            String token = c.req.getHeader("authorization"); 
            if(CheckToken.isValidAdminToken(token, tokenService)){ 
                if(userService.isUsedUsername(username)){
                    userService.validateUser(req, username);
                } else throw new NotFoundException("ERROR: Username is not in the database");
            } else throw new InvalidAuthException("ERROR: Invalid token");
            logger.info("Validation Successful");
            c.status(202);
        } catch (InvalidAuthException e){
            e.printStackTrace();
            c.status(401);
        } catch (NotFoundException e){
            e.printStackTrace();
            c.status(404);
        }  catch (JsonParseException e) {
            logger.info("Malformed Input");
            c.status(400);
            c.json(e);
        }
    }

    //Allows for an admin to change a user's password
    public void setUserPassword(Context c) throws IOException{
        User updatedUser = new User();    
        try {
            UpdateUserRequest req = mapper.readValue(c.req.getInputStream(), UpdateUserRequest.class);
            String username = c.req.getParameter("username");
            String token = c.req.getHeader("authorization");

            //Checks to see if the token owner is an admin and if the password input is valid
            if(CheckToken.isValidAdminToken(token, tokenService)) {
                if(userService.isValidPassword(req.getPassword())) {
                    System.out.println("In here");
                    String hashedPassword = HashString.hashString(req.getPassword());
                    req.setPassword(hashedPassword);
                    updatedUser = userService.updateUserPassword(username, req);
                } else throw new InvalidInputException("ERROR: Passwords must be a minimum of 8 characters, with at least one letter, one number, and one special character");    
            }else throw new InvalidAuthException("ERROR: Invalid token");

            logger.info("Password Change Successful");
            c.json(updatedUser);
            c.status(202);
        } catch (InvalidAuthException e){
            c.status(401);
            e.printStackTrace();
        } catch (InvalidInputException e){
            c.status(401);
            e.printStackTrace();
        }   catch (JsonParseException e) {
            logger.info("Malformed Input");
            c.status(400);
            c.json(e);
        }
    }
    
}