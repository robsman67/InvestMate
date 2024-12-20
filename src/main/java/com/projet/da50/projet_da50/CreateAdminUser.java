package com.projet.da50.projet_da50;

import com.projet.da50.projet_da50.controller.UserController;
import com.projet.da50.projet_da50.model.Role;
import com.projet.da50.projet_da50.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Scanner;

public class CreateAdminUser {

    public static void main(String[] args) {
        UserController userController = new UserController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Création d'un compte administrateur :");

        // Demander le nom d'utilisateur
        System.out.print("Nom d'utilisateur : ");
        String username = scanner.nextLine();

        // Demander l'adresse e-mail
        System.out.print("Adresse e-mail : ");
        String mail = scanner.nextLine();

        // Demander le mot de passe et le hacher
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));

        // Créer l'utilisateur avec le rôle Reader par défaut
        Long adminUserId = userController.createUser(username, hashedPassword, mail);

        if (adminUserId != null) {
            System.out.println("Utilisateur créé avec succès. ID : " + adminUserId);

            // Récupérer l'utilisateur par son ID
            User adminUser = userController.getUserById(adminUserId);
            if (adminUser != null) {
                // Attribuer le rôle Admin
                adminUser.setRole(Role.Admin);

                // Mettre à jour l'utilisateur dans la base de données
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

        // Fermer la SessionFactory
        userController.close();
    }
}