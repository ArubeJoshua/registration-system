package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.dao.UserDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class UserService {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private final UserDAO userDAO;
    private User currentUser;

    public UserService() {
        userDAO = new UserDAO();
    }

    public boolean registerUser (String username, String firstName, String lastName, Date dateOfBirth) {

        User newUser = new User();

        newUser.setUsername(username);
        newUser.setFirstname(firstName);
        newUser.setLastname(lastName);
        newUser.setDateOfBirth(dateOfBirth);

        userDAO.save(newUser);
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



    public List<User> retrieveUsersList() {
        List<User> allUsers = userDAO.getAllUsers();
        if (allUsers.isEmpty()) {
            System.out.println("No users registered.");
        }
        return allUsers;

    }

    public User retrieveUser(String username) {
        currentUser = userDAO.getUserByUsername(username);
        return currentUser;
    }

    public boolean searchUsername(String username){
        // Check if the user exists
        if (!userDAO.isUsernameExists(username)) {
            System.out.println("User not found.");
            return true;
        }
        return false;
    }

    public void updateUser(String oldUsername, String newUsername, String newFirstName, String newLastName, Date newDateOfBirth) {
        currentUser.setUsername(newUsername);
        currentUser.setFirstname(newFirstName);
        currentUser.setLastname(newLastName);
        currentUser.setDateOfBirth(newDateOfBirth);

        userDAO.update(currentUser, oldUsername);

    }

    public boolean deleteUser(String username) {
        if (userDAO.isUsernameExists(username)) {
            userDAO.deleteUser(username);
            return true;
        }
        return false;
    }

    public boolean deleteAllUsers() {
        if (userDAO.containUsers()){
            userDAO.deleteAllUsers();
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
        if (userDAO.isUsernameExists(username)) {
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
