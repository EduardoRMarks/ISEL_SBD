package tests.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import util.PasswordUtil;

class PasswordUtilTest {

	@Test
	void hashPassword_ShouldGenerateHash() {
		// Arrange
		String password = "testPassword";

		// Act
		String hashedPassword = PasswordUtil.hashPassword(password);

		// Assert
		assertNotNull(hashedPassword);
		assertNotEquals(password, hashedPassword); // Check that the hash is different from the plain password
	}

	@Test
	void checkPassword_WithCorrectPassword_ShouldReturnTrue() {
		// Arrange
		String password = "testPassword";
		String hashedPassword = PasswordUtil.hashPassword(password);

		// Act
		boolean result = PasswordUtil.checkPassword(password, hashedPassword);

		// Assert
		assertTrue(result);
	}

	@Test
	void checkPassword_WithIncorrectPassword_ShouldReturnFalse() {
		// Arrange
		String correctPassword = "testPassword";
		String incorrectPassword = "wrongPassword";
		String hashedPassword = PasswordUtil.hashPassword(correctPassword);

		// Act
		boolean result = PasswordUtil.checkPassword(incorrectPassword, hashedPassword);

		// Assert
		assertFalse(result);
	}

	@Test
	void checkPassword_WithNullPassword_ShouldReturnFalse() {
		// Arrange
		String password = "testPassword";
		String hashedPassword = PasswordUtil.hashPassword(password);

		// Act
		boolean result = PasswordUtil.checkPassword(null, hashedPassword);

		// Assert
		assertFalse(result);
	}

	@Test
	void checkPassword_WithNullHashedPassword_ShouldReturnFalse() {
		// Arrange
		String password = "testPassword";

		// Act
		boolean result = PasswordUtil.checkPassword(password, null);

		// Assert
		assertFalse(result);
	}
}
