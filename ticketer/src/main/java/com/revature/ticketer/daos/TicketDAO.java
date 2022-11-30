package com.revature.ticketer.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.revature.ticketer.models.Ticket;
import com.revature.ticketer.utils.ConnectionFactory;

//Returns ticket data from the database
public class TicketDAO implements TemplateDAO<Ticket>{

    @Override
    public void delete(Ticket obj) {
        
    }

    @Override
    public List<Ticket> findAll() {
        return null;
    }

    @Override
    public Ticket findById() {
        return null;
    }

    @Override
    public void save(Ticket obj) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()) { 
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursements (id, amount, submitted, resolved, description, payment_id, author_id, " +
                "resolver_id, status_id, type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, obj.getId());
            ps.setDouble(2, obj.getAmount());
            ps.setTimestamp(3, obj.getSumbittedTime());
            ps.setTimestamp(4, null);
            ps.setString(5, obj.getDescription());
            ps.setString(6, null);
            ps.setString(7, obj.getAuthorId());
            ps.setString(8, null);
            ps.setString(9, obj.getStatus());
            ps.setString(10, obj.getType());
            ps.executeUpdate();
            System.out.println("Worked");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Ticket obj) {
        
    }


    
}
