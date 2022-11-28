package com.revature.ticketer.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.ticketer.models.Role;
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
                User currentUser = new User(rs.getString("id"), rs.getString("username"), rs.getString("email"),
                rs.getString("password"), rs.getString("given_name"), rs.getString("surname"),
                rs.getBoolean("is_active"), rs.getString("role_id"));
                users.add(currentUser);
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
            //Currently the PreparedStatement or database is giving me issues
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (id, username, email, password,"
                + " given_name, surname, is_active, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            //Interpolation which puts values inside of columns
            ps.setString(1, obj.getId());
            ps.setString(2, obj.getUsername());
            ps.setString(3, obj.getEmail());
            ps.setString(4, obj.getPassword());
            ps.setString(5, obj.getGivenName());
            ps.setString(6, obj.getSurname());
            ps.setBoolean(7, obj.isActive());
            ps.setString(8, obj.getRole_id());//fix this
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


    /* return role_id by role -> (EMPLOYEE, FINANCE MANAGER, etc) */
    public String getRoleIdByRole(String role) {
        //ADD A QUERY PREPARED STATEMENT TO DO THIS
        return "";
    }
}
