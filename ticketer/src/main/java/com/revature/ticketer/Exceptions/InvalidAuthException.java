package com.revature.ticketer.Exceptions;

public class InvalidAuthException extends RuntimeException{

    public InvalidAuthException(String errorMessage){
        super(errorMessage);
    }

}
