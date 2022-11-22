package com.revature.ticketer.daos; 

import java.util.List;

/*
 * Generic DAO that can be inferfaced by more 
 * specific DAOs with specific functionality
 */
public interface TemplateDAO<T>{
    
    /*
     * Can Add additional methods to both
     * TicketDAO and UserDAO from here
     */

    void save();
    void delete();
    void update();
    List<T> findAll();
    T findById();
}
