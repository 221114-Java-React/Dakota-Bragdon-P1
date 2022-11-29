package com.revature.ticketer.dtos.requests;

//Used to request a username and password for logging in purposes
public class NewLoginRequest {
    private String username;
    private String password;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "NewLoginRequest [username=" + username + ", password=" + password + "]";
    }
}
