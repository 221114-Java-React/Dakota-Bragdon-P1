package com.revature.ticketer.utils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

//A classed used to encrypt Strings to be stored in the database (and to compare with those stored strings)
public class HashString {
    
    public static String hashString(String wordToHash){
        Properties properties = new Properties();
        String hashedString = "";
        try {
            properties.load(new FileReader("src/main/resources/db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] saltyBytes = DatatypeConverter.parseBase64Binary(properties.getProperty("salt"));
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(saltyBytes);
            byte[] hashedWord = md.digest(wordToHash.getBytes(StandardCharsets.UTF_8)); 
            hashedString = new String(hashedWord);
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return hashedString;
    }
}
