package com.revature.ticketer.Exceptions;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String errorMessage){
        super(errorMessage);
    }
}
