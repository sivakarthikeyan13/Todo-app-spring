package com.todo.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InputValidation {

    private static final String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String nameRegex = "^[A-Za-z0-9'\\s]{1,30}$";
    private static final String passwordRegex = "^(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&-+=()])" + "(?=\\S+$).{8,20}$";
    private static final String taskRegex = "^[\\w\\s]{1,24}$";

    //empty, null field validation
    public void emptyOrNullFieldValidation(List<String > inputList) throws Exception {
        for (String input: inputList) {
            if(input == null || input.isEmpty())
                throw new Exception("enter all fields...");
        }
    }

    //password and confirmPassword matches validation
    public void confirmPasswordMatchValidation (String password, String confirmPassword) throws Exception {
        if (!password.equals(confirmPassword))
            throw new Exception("password and confirm-password does not match... ");
    }

    //to validate single field types
    public void inputFieldTypeValidation(String input, String fieldType) throws Exception {
        switch (fieldType) {
            case "name":
                //name validation
                if (!checkField(input, nameRegex))
                    throw new Exception("Name should be less than 30 character and include only english words and numbers");
                break;
            case "email":
                //Email validation
                if (!checkField(input, emailRegex))
                    throw new Exception(input + " is not a Email id");
                break;
            case "password":
                //Password validation
                if (!checkField(input, passwordRegex))
                    throw new Exception("Password should contain 8-20 char, 1 digit, 1 upper case, 1 lower case, 1 special character and no blank spaces");
                break;
            case "task":
                if (!checkField(input, taskRegex))
                    throw new Exception("Name should be less than 24 characters");
        }
    }

    //METHOD OVERLOADED to check three fields type
    public void inputFieldTypeValidation(String name, String email, String password) throws Exception {
        //name validation
        if (!checkField(name, nameRegex))
            throw new Exception("Name should be less than 30 character and include only english words and numbers");
        //Email validation
        if (!checkField(email, emailRegex))
            throw new Exception(email + " is not a Email id");
        //Password validation
        if (!checkField(password, passwordRegex))
            throw new Exception("Password should contain 8-20 char, 1 digit, 1 upper case, 1 lower case, 1 special character and no blank spaces");
    }

    //check regex match
    public boolean checkField(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
