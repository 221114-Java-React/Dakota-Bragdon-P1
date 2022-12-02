package com.revature.ticketer.models;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String givenName;
    private String surname;
    private boolean isActive;
    private String role_id;
    

    public User(){
        super();
    }


    public User(String id, String username, String email, String password, String givenName, String surname,
            boolean isActive, String role_id) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.givenName = givenName;
        this.surname = surname;
        this.isActive = isActive;
        this.role_id = role_id;
    }

    //Used for listing when listing any user
    public User(String id, String username, String email, String givenName, String surname, boolean isActive,
            String role_id) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.givenName = givenName;
        this.surname = surname;
        this.isActive = isActive;
        this.role_id = role_id;
    }

    public User(String username, boolean isActive) {
        this.username = username;
        this.isActive = isActive;
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


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getGivenName() {
        return givenName;
    }


    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }


    public String getSurname() {
        return surname;
    }


    public void setSurname(String surname) {
        this.surname = surname;
    }


    public boolean isActive() {
        return isActive;
    }


    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


    public String getRole_id() {
        return role_id;
    }


    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }


    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", givenName=" + givenName + ", surname=" + surname + ", isActive=" + isActive + ", role_id="
                + role_id + "]";
    }

    
}
