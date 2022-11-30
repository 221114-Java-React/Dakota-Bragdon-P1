package com.revature.ticketer.dtos.response;

/*
 * This contains all the relevant information for the user. User's don't really need their surname,
 * given_name, password, email, etc for usage for the app. Only the necessary information will be used
 */
public class Principal {
    private String id;
    private String username;
    private String role;

    public Principal(){
        super();
    }

    public Principal(String id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Principal [id=" + id + ", username=" + username + ", role=" + role + "]";
    }
    
    
}
