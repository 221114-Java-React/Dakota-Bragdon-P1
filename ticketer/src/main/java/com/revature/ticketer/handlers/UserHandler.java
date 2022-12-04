package com.revature.ticketer.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.Exceptions.InvalidInputException;
import com.revature.ticketer.Exceptions.InvalidUserException;
import com.revature.ticketer.Exceptions.NotFoundException;
import com.revature.ticketer.dtos.requests.NewUserRequest;
import com.revature.ticketer.models.User;
import com.revature.ticketer.services.TokenService;
import com.revature.ticketer.services.UserService;
import com.revature.ticketer.utils.CheckToken;
import com.revature.ticketer.utils.HashString;

import io.javalin.http.Context;

/*
 * Userhandler class will handle http verbs and endpoints
 */
public class UserHandler {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(TicketHandler.class);
     


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

        try{
            logger.info("Attempting to sign up");
            User createdUser = null;
            
            if(userService.isValidRole(req.getRole())){
                if(userService.isValidUsername(req.getUsername())) {
                    if(!userService.isUsedUsername(req.getUsername())) {
                        if(userService.isValidPassword(req.getPassword1())) {
                            if(userService.isSamePassword(req.getPassword1(), req.getPassword2())) {
                                if(userService.isValidEmail(req.getEmail())) {
                                    if(!userService.isUsedEmail(req.getEmail())) {
                                        String hashedPassword = HashString.hashString(req.getPassword1());//Hashing the password for storage within the db
                                        req.setPassword1(hashedPassword);
                                        createdUser = userService.saveUser(req);
                                    }else throw new InvalidUserException("ERROR: Email is already used");
                                }else throw new InvalidUserException("ERROR: Invalid email");
                            }else throw new InvalidUserException("ERROR: Passwords do not match");
                        } else throw new InvalidInputException("ERROR: Passwords must be a minimum of 8 characters, with at least one letter, one number, and one special character");
                    }else throw new InvalidUserException("ERROR: Username already exists");
                } else throw new InvalidUserException("ERROR: Username must be 8-20 characters long");
            } else throw new InvalidAuthException("ERROR: Only admins can make other admins");

            logger.info("New Account created");
            c.json(createdUser);
            c.status(201); //Created Status Code
        } catch (InvalidUserException e){
            logger.info("Account creation failed");
            c.status(409); //Conflict Status Code
            c.json(e);
        } catch (InvalidAuthException e) {
            logger.info("Account creation failed");
            c.status(401); //Unauthorized
            c.json(e);
        }
    }

    //Returns all users
    public void getAllUsers(Context c){
        try{ 
            List<User> users = null;
            String token = c.req.getHeader("authorization");
            if(CheckToken.isValidAdminToken(token, tokenService)) {
                users = userService.getAllUsers();
            } else throw new InvalidAuthException("ERROR: Only admins can view all users");
            if(users.isEmpty() || users == null) throw new NotFoundException("ERROR: No users found in db"); //shouldn't ever run
            c.json(users);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (NotFoundException e){
            c.status(404);
            c.json(e);
        }
    }

    public void getAllUsersByUsername(Context c){
        try{
            List<User> users = new ArrayList<>();
            //Gets the username from the URI
            String username = c.req.getParameter("username");
            //Gets the token from the authorization header field
            String token = c.req.getHeader("authorization"); 
            if(CheckToken.isValidAdminToken(token, tokenService)) {
                users = userService.getAllUsersByUsername(username);
            } else throw new InvalidAuthException("ERROR: Invalid token");
            if(users.isEmpty()) throw new NotFoundException("ERROR: No users were found");
            c.status(200);
            c.json(users);
        } catch (InvalidAuthException e){
            c.status(401);
            c.json(e);
        } catch (NotFoundException e){
            c.status(404);
            c.json(e);
        } catch (InvalidInputException e){
            c.status(400);
            c.json(e);
        }
    }

}