package com.revature.ticketer.services;

import java.util.List;
import java.util.UUID;

import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.Exceptions.InvalidUserException;
import com.revature.ticketer.daos.UserDAO;
import com.revature.ticketer.dtos.requests.NewLoginRequest;
import com.revature.ticketer.dtos.requests.NewUserRequest;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.models.User;

/*
 * Used to validate and retrieve data from the DAO
 * Service class is essentially an API
 * Service class is an example of Dependency Injection
 * since it relies on UserDAO to function
 * RECALL THE SERVER ANALOGY; A service class
 * ensures data is in an acceptable form prior to passing it to the
 * DAO which will perform under the assumption data has been validated
 */
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /*
     * A DTO is sent instead of a POJO since there are some things you don't want
     * the user to send. Helps limit user control
     */
    public void saveUser(NewUserRequest request){
        List<String> usernames = userDAO.findAllUsernames();
        List<String> emails = userDAO.findAllEmails();

        //Used to ensure username, email, and password are all valid
        //SPLIT THIS UP SO UNIT TESTING CAN BE PERFORMED (Also it's an eye sore)
        if(!isValidUsername(request.getUsername())) throw new InvalidUserException("ERROR: Username must be 8-20 characters long");
        if(usernames.contains(request.getUsername())) throw new InvalidUserException("ERROR: Username already exists");
        if(!isValidPassword(request.getPassword1())) throw new InvalidUserException("ERROR: Passwords must be a minimum of 8 " + 
        "characters, with at least one letter, one number, and one special character");
        if(!request.getPassword1().equals(request.getPassword2())) throw new InvalidUserException("ERROR: Passwords do not match");
        if(!isValidEmail(request.getEmail())) throw new InvalidUserException("ERROR: Invalid email");
        
        if(emails.contains(request.getEmail())) throw new InvalidUserException("ERROR: Email is already used");

        //Creates a new user using the DTO request UUID.randomUUID generates a random id for user
        //User will be given the Default role of Employee, and isActive
        User createdUser = new User(UUID.randomUUID().toString(), request.getUsername(), request.getEmail(),
            request.getPassword1(), request.getGivenName(), request.getSurname(), false , "e58ed763-928c-4155-bee9-fdbaaadc15f3");
        userDAO.save(createdUser);
    }

    public Principal login(NewLoginRequest req){
        User validUser = userDAO.findUserByUserNameAndPassword(req.getUsername(),req.getPassword());
        if (validUser == null) throw new InvalidAuthException("ERROR: Invalid username or password");
        //Last spot is blank because we haven't actually generated an authToken yet.
        return new Principal (validUser.getId(), validUser.getUsername(), validUser.getRole_id());
    }

    //Checks to see if the username is valid
    private boolean isValidUsername(String username) {
        //Checks if the username is 8-20 are removes a lot of special characters
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    //Checks to see if the password is valid
    private boolean isValidPassword(String password) {
        //Checks to see if password is valid. Minimum of 8 characters, 1 letter, 1 num, 1 special char
        //added an extra \ before the d's because of a compiler issue
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    //Checks to see if the email is valid
    private boolean isValidEmail(String email){
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }

    //Encrypts a user's password prior to storing it on the database
    private String hashPassword(String password){
        return "";
    }

    public List<User> getAllUsers(){
        return userDAO.findAll();
    }

    public List<User> getAllUsersByUsername(String username){
        return userDAO.findUsersByUsername(username);
    }
}
