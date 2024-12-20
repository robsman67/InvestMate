package controllerTest;

import com.projet.da50.projet_da50.controller.LoginErrorHandler;
import com.projet.da50.projet_da50.controller.UserController;
import com.projet.da50.projet_da50.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginErrorHandlerTest {

    private LoginErrorHandler loginErrorHandler;
    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        loginErrorHandler = new LoginErrorHandler();
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
    void testValidateAuthenticationFields_ValidCredentials() {
        assertEquals("Valid credentials.", loginErrorHandler.validateAuthenticationFields(user.getUsername(), "testPassword"));
    }

    @Test
    void testValidateAuthenticationFields_InvalidPassword() {
        assertEquals("Invalid username or password.", loginErrorHandler.validateAuthenticationFields(user.getUsername(), "wrongPassword"));
    }

    @Test
    void testValidateAuthenticationFields_UserDoesNotExist() {
        assertEquals("Invalid username or password.", loginErrorHandler.validateAuthenticationFields("nonExistentUser", "anyPassword"));
    }

    @Test
    void testValidateCreateAccountFields_ValidCredentials() {
        assertEquals("Valid credentials.", loginErrorHandler.validateCreateAccountFields("newUser", "newPassword", "newuser@example.com"));
    }

    @Test
    void testValidateCreateAccountFields_UsernameAlreadyTaken() {
        assertEquals("This username is already taken.", loginErrorHandler.validateCreateAccountFields(user.getUsername(), "anyPassword", "unique@example.com"));
    }

    @Test
    void testValidateCreateAccountFields_MailAlreadyUsed() {
        assertEquals("This mail is already used.", loginErrorHandler.validateCreateAccountFields("uniqueUser", "anyPassword", user.getMail()));
    }

    @Test
    void testValidateCreateAccountFields_InvalidMailFormat() {
        assertEquals("Mail format is invalid.", loginErrorHandler.validateCreateAccountFields("newUser", "newPassword", "invalidMail"));
    }

    @Test
    void testValidateCreateAccountFields_ShortPassword() {
        assertEquals("Password should be at least 6 characters long.", loginErrorHandler.validateCreateAccountFields("newUser", "short", "newuser@example.com"));
    }

    @Test
    void testValidateForgotPasswordFields_ValidMail() {
        assertEquals("Valid credentials.", loginErrorHandler.validateForgotPasswordFields(user.getMail()));
    }

    @Test
    void testValidateForgotPasswordFields_InvalidMailFormat() {
        assertEquals("Mail format is invalid.", loginErrorHandler.validateForgotPasswordFields("invalidMail"));
    }

    @Test
    void testValidateForgotPasswordFields_MailDoesNotExist() {
        assertEquals("This mail does not exist", loginErrorHandler.validateForgotPasswordFields("nonexistent@example.com"));
    }
}