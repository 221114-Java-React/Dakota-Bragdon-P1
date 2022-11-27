package com.revature.ticketer.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.ticketer.models.User;
import com.revature.ticketer.utils.ConnectionFactory;

//Returns user data from the database
public class UserDAO implements TemplateDAO<User>{

    @Override
    public void delete(User obj) {
        
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            //prepares an SQL command
            PreparedStatement ps = con.prepareStatement("SELECT * from users");
            ResultSet rs = ps.executeQuery();

            //CURRENTLY AN INFINITE WHILE LOOP. PROPERLY FIX THIS BY CORRECTLY ADDING LATER
            while(rs.next()){
                //user currentUser = new User(rs.getString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User findById() {
        return null;
    }

    @Override
    public void save(User obj) {
        //This block of code will be necessary every time we need a connection
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            //Prepares some string/input to be stored within a database
            //ALWAYS START WITH A PREPARED STATEMENT
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (id, username, password, role) " + 
                "VALUES (?, ?, ?, ?)");
            //Interpolation which puts values inside of columns
            ps.setString(1, obj.getId());//Saves id
            ps.setString(2, obj.getUsername());//Saves username ...
            ps.setString(3, obj.getPassword());
            ps.setString(4, obj.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }      
    }

    @Override
    public void update(User obj) {
        
    }

    //Returns a list of everyusername in the database
    public List<String> findAllUsernames(){
        List<String> usernames = new ArrayList<>();
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            //prepares an SQL command
            PreparedStatement ps = con.prepareStatement("SELECT (username) from users");
            ResultSet rs = ps.executeQuery();

            //This set of data is basically just a row
            while(rs.next()){
                //Gets the username from the next set of data and adds it to a list
                String currentName = rs.getString("username");
                usernames.add(currentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
    }
    
}
