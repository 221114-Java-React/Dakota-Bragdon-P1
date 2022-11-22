package com.revature.ticketer.Exceptions;

public class InvalidInputException extends RuntimeException{
    
    public InvalidInputException(String errorMessage){
        super(errorMessage);
    }
}
