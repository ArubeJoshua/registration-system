package org.pahappa.systems.registrationapp.services;
import org.pahappa.systems.registrationapp.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UserService {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private final HashMap<String, User> users = new HashMap<>();

    public boolean registerUser(String username, String firstName, String lastName, Date dob) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setFirstname(firstName);
        newUser.setLastname(lastName);
        newUser.setDateOfBirth(dob);

        if (users.containsKey(username)) {
            System.out.println("This username is already taken. Please try again.");
            return false;
        }else if (users.values().stream().anyMatch(user -> user.equals(newUser))) {
            System.out.println("A user with the same details already exists. Please try again.");
            return false;
        }
        users.put(username, newUser);
        return true;
    }

    // Method to parse a date string into a Date object
    public Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in dd-MM-yyyy format.");
        }
        return date;
    }

    public HashMap<String, User> retrieveUsersList() {
        return users;
    }

    public User retrieveUser(String username) {
        return users.get(username);
    }

    public void updateUser(String username ,String newFirstName, String newLastName, Date newDob) {
        // Check if the user exists
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }
        // Retrieve the user
        User user = users.get(username);

        // Update the user's details
        user.setUsername(username);
        user.setFirstname(newFirstName);
        user.setLastname(newLastName);
        user.setDateOfBirth(newDob);
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
