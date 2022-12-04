package com.revature.ticketer.utils;

import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckToken {
    private static final Logger logger = LoggerFactory.getLogger(CheckToken.class);

    //Checks for valid employee priviledges
    public static boolean isValidEmployeeToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f3")) {
            return false;
        }
        return true;
    }

    //Checks for valid manager priviledges
    public static boolean isValidManagerToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f4")) {
            return false;
        }
        return true;
    }

    //Checks for valid admin priviledges
    public static boolean isValidAdminToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f5")) {
            return false;
        }
        return true;
    }

    //Gets the Token's Owner's ID
    public static String getOwner(String token, TokenService tokenService){
        Principal principal = tokenService.extractRequesterDetails(token);
        String id = principal.getId();
        if (id == null || id.equals("")) return "";
        return id;
    }

    //Gets the Token's Owner's username
    public static String getOwnerUsername(String token, TokenService tokenService){
        Principal principal = tokenService.extractRequesterDetails(token);
        String username = principal.getUsername();
        if (username == null || username.equals("")) return "";
        return username;
    }

    //Gets the Token's Owner's Role
    public static String getOwnerRole(String token, TokenService tokenService){
        Principal principal = tokenService.extractRequesterDetails(token);
        String role = principal.getRole();
        if (role == null || role.equals("")) return "";
        return role;
    }

    public static boolean isEmptyToken(String token){
        if (token == null || token.isEmpty()) {
            logger.info("No token detected");
            return true;  
        } else {
            return false;
        }
    }
}
