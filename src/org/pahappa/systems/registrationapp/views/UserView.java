package org.pahappa.systems.registrationapp.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.models.User;

public class UserView {

    private final Scanner scanner;

    public UserView(){
        this.scanner = new Scanner(System.in);
    }

    public static UserService userService = new UserService();
    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

    public void displayMenu() {
        System.out.println("********* User Registration System *********");
        boolean running = true;

        while (running) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Register a user");
            System.out.println("2. Display all users");
            System.out.println("3. Get a user of username");
            System.out.println("4. Update user details of username");
            System.out.println("5. Delete User of username");
            System.out.println("6. Delete all users");
            System.out.println("7. Exit");
            try{
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        displayAllUsers();
                        break;
                    case 3:
                        getUserOfUsername();
                        break;
                    case 4:
                        updateUserOfUsername();
                        break;
                    case 5:
                        deleteUserOfUsername();
                        break;
                    case 6:
                        deleteAllUsers();
                        break;
                    case 7:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }catch (Exception e){
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume the newline character
            }
        }
    }

    private void registerUser() {
        String username;
        String first_name;
        String last_name;
        Date dateOfBirth;
        do {System.out.println("Enter username: ");
             username = scanner.nextLine(); }while(userService.checkUsername(username));
        do {System.out.println("Enter first name: ");
             first_name = scanner.nextLine(); }while(userService.checkname(first_name));
        do {System.out.println("Enter last name: ");
             last_name = scanner.nextLine(); }while(userService.checkname(last_name));
        do {System.out.println("Enter date of birth (dd-MM-yyyy): ");
            String dateStr = scanner.nextLine();
            dateOfBirth = userService.parseDate(dateStr); }while(userService.checkDate(dateOfBirth));

        if (userService.registerUser(username, first_name, last_name, dateOfBirth)) {
            System.out.println("User registered successfully.");
        }
    }

    private void displayAllUsers() {
        HashMap<String, User> users = userService.retrieveUsersList();
        for (User user : users.values()) {

            String formattedDob = outputFormat.format(user.getDateOfBirth());
            System.out.println(user.getUsername() + ": " + user.getFirstname() + " " + user.getLastname() + ", DOB: " + formattedDob);

        }
    }

    private void getUserOfUsername() {
        System.out.println("Enter username: ");
        String username = scanner.next();
        User user = userService.retrieveUser(username);
        if (user != null) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("First Name: " + user.getFirstname());
            System.out.println("Last Name: " + user.getLastname());
            String formattedDob = outputFormat.format(user.getDateOfBirth());
            System.out.println("Date of Birth: " + formattedDob);
        } else {
            System.out.println("User not found.");
        }
    }

    private void updateUserOfUsername() {

        // Prompt for new user details
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter new username: ");
        String newUsername = scanner.next();
        System.out.println("Enter new first name: ");
        String newFirstName = scanner.next();
        System.out.println("Enter new last name: ");
        String newLastName = scanner.next();
        System.out.println("Enter new date of birth (dd-MM-yyyy): ");
        String newDateStr = scanner.next();
        Date newDob = userService.parseDate(newDateStr);

        // Call the updateUser method with the provided details
        userService.updateUser(username, newUsername, newFirstName, newLastName, newDob);
    }

    private void deleteUserOfUsername() {
        System.out.println("Enter username: ");
        String username = scanner.next();
        if (userService.deleteUser(username)) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    private void deleteAllUsers() {
        userService.deleteAllUsers();
        System.out.println("User List successfully deleted");
    }
}