package org.pahappa.systems.registrationapp.services;
import org.pahappa.systems.registrationapp.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UserService {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private final HashMap<String, User> users = new HashMap<>();

    public boolean registerUser(String username, String firstName, String lastName, Date dateOfBirth) {

        User newUser = new User();

        newUser.setUsername(username);
        newUser.setFirstname(firstName);
        newUser.setLastname(lastName);
        newUser.setDateOfBirth(dateOfBirth);

        if (users.containsKey(username) || users.values().stream().anyMatch(user -> user.equals(newUser))) {
            System.out.println("This username is already taken or a user with the same details already exists. Please try again.");
            return false;
        }

        users.put(username, newUser);
        return true;
    }

    public Date parseDate(String dateString) {
        if (dateString.isEmpty()) {
            System.out.println("Date cannot be empty.");
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false); //Strict restrictions to the date format

        try {
            Date date = dateFormat.parse(dateString);
            // Get today's date
            Date today = new Date();

            // Ensure the date is not in the future
            if (date.after(today)) {
                System.out.println("Date of birth cannot be in the future.");
                return null;
            }
            return date;

        } catch (ParseException e) {
            System.out.println("Invalid date!!!!!");
            return null;
        }
    }



        public HashMap<String, User> retrieveUsersList() {
        if (users.isEmpty()) {
            System.out.println("No users registered.");
        }
        return users;

    }

    public User retrieveUser(String username) {
        return users.get(username);
    }

    public boolean searchUsername(String username){
        // Check if the user exists
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return true;
        }
        return false;
    }

    public void updateUser(String username, String newUsername, String newFirstName, String newLastName, Date newDateOfBirth) {

        // Retrieve the user
        User user = users.get(username);

        // Update the user's details
        user.setUsername(newUsername);
        user.setFirstname(newFirstName);
        user.setLastname(newLastName);
        user.setDateOfBirth(newDateOfBirth);
    }

    public boolean deleteUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            return true;
        }
        return false;
    }

    public boolean deleteAllUsers() {
        if (!users.isEmpty()){
            users.clear();
            return true;
        }
        return false;
    }

    public boolean checkUsername(String username) {
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!");
            return true;
        }
        if (username.length() < 4) {
            System.out.println("Username should be at least 4 characters");
            return true;
        }
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different one.");
            return true;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")){
            System.out.println("Username must only contain alphanumeric characters and underscores");
            return true;
        }

        return false;
    }


    public boolean checkname(String name) {

        String regularTextPattern = "^[a-zA-Z]+$";
        if(name.isEmpty()){
            System.out.println("Name cannot be empty!");
            return true;
        }   // Validate firstName, and lastName for regular text (only alphabets)
        if (!name.matches(regularTextPattern)) {
            System.out.println("first name and last name should contain only alphabetic characters.");
            return true;
        }
        return false;
    }

    public boolean checkDate(Date date) {
        return date == null;
    }

}
