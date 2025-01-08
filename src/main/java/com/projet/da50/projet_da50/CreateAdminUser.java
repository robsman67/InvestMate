package com.projet.da50.projet_da50;

import com.projet.da50.projet_da50.controller.UserController;
import com.projet.da50.projet_da50.model.Role;
import com.projet.da50.projet_da50.model.User;
import java.util.Scanner;

public class CreateAdminUser {

    public static void main(String[] args) {
        UserController userController = new UserController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Création d'un compte administrateur :");

        // Ask for the username
        System.out.print("Nom d'utilisateur : ");
        String username = scanner.nextLine();

        // Ask for the email address
        System.out.print("Adresse e-mail : ");
        String mail = scanner.nextLine();

        // Ask for the password
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        // Create the user
        Long adminUserId = userController.createUser(username, password, mail);

        if (adminUserId != null) {
            System.out.println("Utilisateur créé avec succès. ID : " + adminUserId);

            // Retrieve the user
            User adminUser = userController.getUserById(adminUserId);
            if (adminUser != null) {
                // Assign the Admin role to the user
                adminUser.setRole(Role.Admin);

                // Update the user
                if (userController.updateUser(adminUser)) {
                    System.out.println("Rôle Admin attribué à l'utilisateur.");
                } else {
                    System.err.println("Erreur lors de la mise à jour du rôle de l'utilisateur.");
                }
            } else {
                System.err.println("Erreur lors de la récupération de l'utilisateur après la création.");
            }
        } else {
            System.err.println("Erreur lors de la création de l'utilisateur administrateur.");
        }

        // Close the scanner and the user controller
        userController.close();
    }
}