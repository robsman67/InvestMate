package controllerTest;

import com.projet.da50.projet_da50.controller.ErrorHandler;
import com.projet.da50.projet_da50.controller.UserController;
import com.projet.da50.projet_da50.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlerTest {

    private ErrorHandler errorHandler;
    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        errorHandler = new ErrorHandler();
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
        assertEquals("Valid credentials.", errorHandler.validateAuthenticationFields(user.getUsername(), "testPassword"));
    }

    @Test
    void testValidateAuthenticationFields_InvalidPassword() {
        assertEquals("Invalid username or password.", errorHandler.validateAuthenticationFields(user.getUsername(), "wrongPassword"));
    }

    @Test
    void testValidateAuthenticationFields_UserDoesNotExist() {
        assertEquals("Invalid username or password.", errorHandler.validateAuthenticationFields("nonExistentUser", "anyPassword"));
    }

    @Test
    void testValidateCreateAccountFields_ValidCredentials() {
        assertEquals("Valid credentials.", errorHandler.validateCreateAccountFields("newUser", "newPassword", "newuser@example.com"));
    }

    @Test
    void testValidateCreateAccountFields_UsernameAlreadyTaken() {
        assertEquals("This username is already taken.", errorHandler.validateCreateAccountFields(user.getUsername(), "anyPassword", "unique@example.com"));
    }

    @Test
    void testValidateCreateAccountFields_MailAlreadyUsed() {
        assertEquals("This mail is already used.", errorHandler.validateCreateAccountFields("uniqueUser", "anyPassword", user.getMail()));
    }

    @Test
    void testValidateCreateAccountFields_InvalidMailFormat() {
        assertEquals("Mail format is invalid.", errorHandler.validateCreateAccountFields("newUser", "newPassword", "invalidMail"));
    }

    @Test
    void testValidateCreateAccountFields_ShortPassword() {
        assertEquals("Password should be at least 6 characters long.", errorHandler.validateCreateAccountFields("newUser", "short", "newuser@example.com"));
    }

    @Test
    void testValidateForgotPasswordFields_ValidMail() {
        assertEquals("Valid credentials.", errorHandler.validateForgotPasswordFields(user.getMail()));
    }

    @Test
    void testValidateForgotPasswordFields_InvalidMailFormat() {
        assertEquals("Mail format is invalid.", errorHandler.validateForgotPasswordFields("invalidMail"));
    }

    @Test
    void testValidateForgotPasswordFields_MailDoesNotExist() {
        assertEquals("This mail does not exist", errorHandler.validateForgotPasswordFields("nonexistent@example.com"));
    }
}