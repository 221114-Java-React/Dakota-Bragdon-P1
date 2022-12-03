package com.revature.ticketer.dtos.requests;

public class ValidateNewUserRequest {
    boolean isActive;

    public ValidateNewUserRequest(){
        super();
    }

    public ValidateNewUserRequest(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
