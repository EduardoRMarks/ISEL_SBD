package tests.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.User;
import model.UserRole;

class UserTest {

    @Test
    void userConstructor_ShouldInitializeFields() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        UserRole role = UserRole.CLIENT;

        // Act
        User user = new User(username, password, role);

        // Assert
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        User user = new User("username", "password", UserRole.ADMINISTRATOR);

        // Act
        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setRole(UserRole.PERSONAL_TRAINER);

        // Assert
        assertEquals("newUsername", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals(UserRole.PERSONAL_TRAINER, user.getRole());
    }
}