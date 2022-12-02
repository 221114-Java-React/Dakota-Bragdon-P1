package com.revature.ticketer.Exceptions;

public class InvalidActionException extends RuntimeException{
    
    public InvalidActionException(String errorMessage){
        super(errorMessage);
    }
}
