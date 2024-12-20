package controllerTest;

import com.projet.da50.projet_da50.model.User;
import com.projet.da50.projet_da50.controller.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private User user;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        // Crée un utilisateur avec des données de test uniques
        Long userId = userController.createUser("testUser", "testPassword", "test@example.com");
        assertNotNull(userId, "User creation should return a non-null ID");
        user = userController.getUserById(userId);
    }

    @AfterEach
    void tearDown() {
        // Supprime l'utilisateur après chaque test
        if (user != null && user.getId() != null) {
            userController.deleteUserById(user.getId());
        }
    }

    @Test
    void testFindUserByUsername_UserExists() {
        User foundUser = userController.findUserByUsername(user.getUsername());
        assertNotNull(foundUser, "User should be found by username");
        assertEquals(user.getUsername(), foundUser.getUsername(), "Found user should have the same username");
    }

    @Test
    void testFindUserByUsername_UserDoesNotExist() {
        User foundUser = userController.findUserByUsername("nonExistentUser");
        assertNull(foundUser, "No user should be found with a non-existent username");
    }

    @Test
    void testFindUserByMail_UserExists() {
        User foundUser = userController.findUserByMail(user.getMail());
        assertNotNull(foundUser, "User should be found by email");
        assertEquals(user.getMail(), foundUser.getMail(), "Found user should have the same email");
    }

    @Test
    void testFindUserByMail_UserDoesNotExist() {
        User foundUser = userController.findUserByMail("nonexistent@example.com");
        assertNull(foundUser, "No user should be found with a non-existent email");
    }

    @Test
    void testCreateUser_Success() {
        Long newUserId = userController.createUser("newUser", "newPassword", "newuser@example.com");
        assertNotNull(newUserId, "New user creation should return a non-null ID");
        User newUser = userController.getUserById(newUserId);
        assertNotNull(newUser, "New user should be found in the database");
        assertEquals("newUser", newUser.getUsername(), "New user should have the correct username");
        // Nettoyage
        userController.deleteUserById(newUserId);
    }

    @Test
    void testCreateUser_UsernameAlreadyExists() {
        Long existingUserId = userController.createUser(user.getUsername(), "anyPassword", "unique@example.com");
        assertNull(existingUserId, "User creation should fail when username already exists");
    }

    @Test
    void testCreateUser_MailAlreadyExists() {
        Long existingUserId = userController.createUser("uniqueUser", "anyPassword", user.getMail());
        assertNull(existingUserId, "User creation should fail when email already exists");
    }

    @Test
    void testVerifyUserCredentials_ValidCredentials() {
        assertTrue(userController.verifyUserCredentials(user.getUsername(), "testPassword"), "Authentication should succeed with valid credentials");
    }

    @Test
    void testVerifyUserCredentials_InvalidPassword() {
        assertFalse(userController.verifyUserCredentials(user.getUsername(), "wrongPassword"), "Authentication should fail with invalid password");
    }

    @Test
    void testVerifyUserCredentials_UserDoesNotExist() {
        assertFalse(userController.verifyUserCredentials("nonExistentUser", "anyPassword"), "Authentication should fail when user does not exist");
    }

    @Test
    void testDeleteUserById_UserExists() {
        boolean isDeleted = userController.deleteUserById(user.getId());
        assertTrue(isDeleted, "User should be successfully deleted");
        assertNull(userController.getUserById(user.getId()), "Deleted user should not be found");
    }

    @Test
    void testDeleteUserById_UserDoesNotExist() {
        boolean isDeleted = userController.deleteUserById(-1L);
        assertFalse(isDeleted, "Deletion should fail when user does not exist");
    }

    @Test
    void testGetUserById_UserExists() {
        User retrievedUser = userController.getUserById(user.getId());
        assertNotNull(retrievedUser, "User should be retrieved by ID");
        assertEquals(user.getUsername(), retrievedUser.getUsername(), "Retrieved user should have the correct username");
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        User retrievedUser = userController.getUserById(-1L);
        assertNull(retrievedUser, "No user should be retrieved with a non-existent ID");
    }
}