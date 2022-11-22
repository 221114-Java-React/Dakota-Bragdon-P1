package com.revature.ticketer.services;

import com.revature.ticketer.daos.UserDAO;

/*
 * Used to validate and retrieve data from the DAO
 * Service class is essentially an API
 * Service class is an example of Dependency Injection
 * since it relies on UserDAO to function
 */
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

}
