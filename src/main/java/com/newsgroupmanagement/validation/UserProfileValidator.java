package com.newsgroupmanagement.validation;

import com.newsgroupmanagement.dto.UserProfileDto;
import com.newsgroupmanagement.enums.Gender;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserProfileValidator {

    public static Map<String, String> validateProfileFields(UserProfileDto user) {
        Map<String, String> profileErrors = new HashMap<>();

        // FirstName validation
        String patternFirstName = "^[A-Za-z]{1,30}$";
        if(!user.getFirstName().isEmpty() && !Pattern.matches(patternFirstName,user.getFirstName())) {
            profileErrors.put("firstName","First name is not valid.");
        }

        // LastName validation
        String patternLastName = "^[A-Za-z]{1,30}$";
        if(!user.getLastName().isEmpty() && !Pattern.matches(patternLastName,user.getLastName())) {
            profileErrors.put("lastName","Last name is not valid.");
        }

        // Phone Validation
        String patternPhone = "^[6-9][0-9]{9}$";
        if(!user.getPhone().isEmpty() && !Pattern.matches(patternPhone,user.getPhone())) {
            profileErrors.put("phone","Enter a valid 10-digit phone number.");
        }

        // City Validation
        String patternCity = "^[A-Za-z]{1,40}$";
        if(!user.getCity().isEmpty() && !Pattern.matches(patternCity,user.getCity())) {
            profileErrors.put("city","City is not valid.");
        }

        // State Validation
        String patternState = "^[A-Za-z]{1,40}$";
        if(!user.getState().isEmpty() && !Pattern.matches(patternState,user.getState())) {
            profileErrors.put("state","State is not valid.");
        }

        // Birthdate Validation
        if (user.getDob() != null) {
            LocalDate today = LocalDate.now();

            if (!user.getDob().isBefore(today)) {
                profileErrors.put("dob","Dob is not valid.");
            }
        }

        // Gender Validation
        if(user.getGender() != null && !user.getGender().equals(Gender.MALE) && !user.getGender().equals(Gender.FEMALE) && !user.getGender().equals(Gender.OTHER)) {
            profileErrors.put("gender","Gender is not valid.");
        }

        return profileErrors;
    }
}
