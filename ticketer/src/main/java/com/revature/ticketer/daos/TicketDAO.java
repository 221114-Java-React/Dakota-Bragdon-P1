package com.revature.ticketer.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.ticketer.models.Ticket;
import com.revature.ticketer.utils.ConnectionFactory;

//Returns ticket data from the database
public class TicketDAO implements TemplateDAO<Ticket>{

    @Override
    public void delete(Ticket obj) {
        
    }

    @Override
    //Finds a ticket based on an ID
    public Ticket findById(String id) {
        Ticket ticket = new Ticket();
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                 ticket = new Ticket(rs.getString("id"), rs.getDouble("amount"), rs.getTimestamp("submitted"),
                    rs.getTimestamp("resolved"), rs.getString("description"),
                    rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"),
                    rs.getString("status_id"), rs.getString("type_id"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ticket;
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
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Ticket obj) {
        
    }

    //Resolves a Ticket
    public void resolve(Ticket obj){
        
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            //Updates the ticket
            PreparedStatement ps = con.prepareStatement("UPDATE reimbursements SET resolved = ?, resolver_id = ?, status_id = ? WHERE id = ?");
            ps.setTimestamp(1, obj.getResolveTime());
            ps.setString(2, obj.getResolverId());
            ps.setString(3, obj.getStatus());
            ps.setString(4, obj.getId());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Returns all tickets
    @Override
    public List<Ticket> findAll(){
        List<Ticket> tickets = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements ORDER BY status_id, type_id, amount DESC");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Ticket ticket = new Ticket(rs.getString("id"), rs.getDouble("amount"), rs.getTimestamp("submitted"),
                    rs.getTimestamp("resolved"), rs.getString("description"),
                    rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"),
                    rs.getString("status_id"), rs.getString("type_id"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    //Returns all pending tickets
    public List<Ticket> findAllPendingTickets(String status){
        List<Ticket> tickets = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE status_id = ? ORDER BY author_id, type_id, amount DESC");
            ps.setString(1,status);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Ticket ticket = new Ticket(rs.getString("id"), rs.getDouble("amount"), rs.getTimestamp("submitted"),
                    rs.getTimestamp("resolved"), rs.getString("description"),
                    rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"),
                    rs.getString("status_id"), rs.getString("type_id"));
                tickets.add(ticket);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tickets;
    }

    //Finds all the tickets a manager has resolved
    public List<Ticket> findAllResolvedTickets(String resolverId){
        List<Ticket> tickets = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE resolver_id = ? ORDER BY status_id, author_id, amount DESC");
            ps.setString(1,resolverId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Ticket ticket = new Ticket(rs.getString("id"), rs.getDouble("amount"), rs.getTimestamp("submitted"),
                    rs.getTimestamp("resolved"), rs.getString("description"),
                    rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"),
                    rs.getString("status_id"), rs.getString("type_id"));
                tickets.add(ticket);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return tickets;
    }

    //Gets all the tickets of a specific user
    public List<Ticket> findAllUserTickets(String id){
        List<Ticket> tickets = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * from reimbursements WHERE author_id = ? order by status, type_id, amount DESC");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Ticket ticket = new Ticket(rs.getString("id"), rs.getDouble("amount"), rs.getTimestamp("submitted"),
                    rs.getTimestamp("resolved"), rs.getString("description"),
                    rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"),
                    rs.getString("status_id"), rs.getString("type_id"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    //Converts a type into its corresponding UUID
    public String getTypeIdByType(String type) {
        String typeId = "";
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (id) FROM reimbursement_types WHERE type = ?");
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                typeId = rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeId;
    }

    //Converts a status into its corresponding UUID
    public String getStatusIdByStatus(String status) {
        String statusId = "";
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (id) FROM reimbursement_statuses WHERE status = ?");
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                statusId = rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusId;
    }

}
