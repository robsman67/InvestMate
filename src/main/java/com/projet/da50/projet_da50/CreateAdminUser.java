package com.projet.da50.projet_da50;

import com.projet.da50.projet_da50.controller.user.UserController;
import com.projet.da50.projet_da50.model.user.Role;
import com.projet.da50.projet_da50.model.user.User;
import java.util.Scanner;

/**
 * A utility class to create an administrator user account for the application.
 */
public class CreateAdminUser {

    /**
     * The main method to create an administrator user by taking input from the console.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        UserController userController = new UserController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Creating an administrator account:");

        // Ask for the username
        System.out.print("Username: ");
        String username = scanner.nextLine();

        // Ask for the email address
        System.out.print("Email address: ");
        String mail = scanner.nextLine();

        // Ask for the password
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Create the user
        Long adminUserId = userController.createUser(username, password, mail);

        if (adminUserId != null) {
            System.out.println("User successfully created. ID: " + adminUserId);

            // Retrieve the user
            User adminUser = userController.getUserById(adminUserId);
            if (adminUser != null) {
                // Assign the Admin role to the user
                adminUser.setRole(Role.Admin);

                // Update the user
                if (userController.updateUser(adminUser)) {
                    System.out.println("Admin role assigned to the user.");
                } else {
                    System.err.println("Error while updating the user's role.");
                }
            } else {
                System.err.println("Error while retrieving the user after creation.");
            }
        } else {
            System.err.println("Error while creating the administrator user.");
        }

        // Close the scanner and the user controller
        userController.close();
    }
}
