package com.revature.ticketer.services;

import java.util.List;
import java.util.UUID;

import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.daos.UserDAO;
import com.revature.ticketer.dtos.requests.NewLoginRequest;
import com.revature.ticketer.dtos.requests.NewUserRequest;
import com.revature.ticketer.dtos.requests.UpdateUserRequest;
import com.revature.ticketer.dtos.requests.ValidateNewUserRequest;
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
    public User saveUser(NewUserRequest req){

        //Allows for a user to input human text, rather than an incomprehensible UUID
        String roleId = userDAO.getRoleIdByRole(req.getRole());

        //Creates a new user using the information they input, a UUID, and sets isActive to false
        User createdUser = new User(UUID.randomUUID().toString(), req.getUsername(), req.getEmail(),
            req.getPassword1(), req.getGivenName(), req.getSurname(), false , roleId);
        userDAO.save(createdUser);
        return createdUser;
    }

    //Validates (or invalidates) username
    public void validateUser(ValidateNewUserRequest request, String username){
        User validatedUser = new User(username, request.isActive());
        userDAO.validate(validatedUser);
    }

    //Logs the user in by generating a principal
    public Principal login(NewLoginRequest req){
        User validUser = userDAO.findUserByUserNameAndPassword(req.getUsername(),req.getPassword());
        if (validUser == null) throw new InvalidAuthException("ERROR: Invalid username or password");
        if(!validUser.isActive()) throw new InvalidAuthException("ERROR: You have not been granted permission. Ask your admin to verify you");
        return new Principal (validUser.getId(), validUser.getUsername(), validUser.getRole_id());
    }

    //Sets a user's password (Admin changes a user password)
    public User updateUserPassword(String username, UpdateUserRequest req){
        return userDAO.setUserPassword(username, req.getPassword());
    }

    public List<User> getAllUsers(){
        return userDAO.findAll();
    }

    public List<User> getAllUsersByUsername(String username){
        return userDAO.findUsersByUsername(username);
    }

    //User updates their own password
    public void updateUserPassword(){
        
    }

    //Checks to see if the username is valid. Must be 8-20 characters long
    public boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    //Checks to see if password is valid. Minimum of 8 characters, 1 letter, 1 num, 1 special char
    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    //Checks to see if the username is already in the database
    public boolean isUsedUsername(String username){
        List<String> usernames = userDAO.findAllUsernames();
        return usernames.contains(username);
    }

    //Checks to see if the email is already in the database
    public boolean isUsedEmail(String email){
        List<String> emails = userDAO.findAllEmails();
        return emails.contains(email);
    }

    //Checks to see if the two passwords are the same
    public boolean isSamePassword(String password1, String password2){
        return (password1.equals(password2));
    }

    public boolean isValidRole(String role){
        return (role.equals("EMPLOYEE") || role.equals("MANAGER"));
    }

    //Checks to see if the email is valid
    public boolean isValidEmail(String email){
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }

    //Checks to see if the username is in the database
    public String getIdfromUsername(String username){
        return userDAO.findIdfromUsername(username);
    }
}
