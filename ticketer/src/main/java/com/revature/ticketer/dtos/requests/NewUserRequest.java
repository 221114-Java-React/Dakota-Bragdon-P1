package com.revature.ticketer.dtos.requests;

//This request will be sent when attempting to save a new user
public class NewUserRequest{
    private String username;
    private String email;
    private String password1;
    private String password2;
    private String givenName;
    private String surname;

    public NewUserRequest(){
        super();
    }

    //Will prompt the user for these things. Consider removing isActive since that should be updated automatically
    public NewUserRequest(String username, String email, String password1, String password2, String givenName,
            String surname) {
        this.username = username;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.givenName = givenName;
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "NewUserRequest{username=" + username + ", password1=" + password1 + ", password2=" + password2 + "}";
    }
}
