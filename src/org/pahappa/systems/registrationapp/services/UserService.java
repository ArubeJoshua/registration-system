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

        // Validate username, firstName, and lastName for regular text (only alphabets)
        String regularTextPattern = "^[a-zA-Z]+$";
        if ( !firstName.matches(regularTextPattern) || !lastName.matches(regularTextPattern)) {
            System.out.println("first name and last name should contain only alphabetic characters.");
            return false;
        }

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

    // Method to parse a date string into a Date object
    public Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in dd-MM-yyyy format.");
        }
        return date;
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

    public void updateUser(String username, String newUsername,String newFirstName, String newLastName, Date newDateOfBirth) {
        // Check if the user exists
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }
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

    public void deleteAllUsers() {
        users.clear();
    }

}
