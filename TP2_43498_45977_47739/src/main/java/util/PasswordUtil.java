package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordUtil {

	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int SALT_BYTE_SIZE = 16;
	private static final int HASH_BYTE_SIZE = 32;
	private static final int ITERATIONS = 10000;

	public static String hashPassword(String password) {
		try {
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[SALT_BYTE_SIZE];
			random.nextBytes(salt);

			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_BYTE_SIZE * 8);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			byte[] hash = factory.generateSecret(spec).getEncoded();

			byte[] combined = new byte[salt.length + hash.length];
			System.arraycopy(salt, 0, combined, 0, salt.length);
			System.arraycopy(hash, 0, combined, salt.length, hash.length);

			return Base64.getEncoder().encodeToString(combined);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		try {
			byte[] combined = Base64.getDecoder().decode(hashedPassword);
			byte[] salt = new byte[SALT_BYTE_SIZE];
			byte[] hash = new byte[combined.length - SALT_BYTE_SIZE];

			System.arraycopy(combined, 0, salt, 0, SALT_BYTE_SIZE);
			System.arraycopy(combined, SALT_BYTE_SIZE, hash, 0, HASH_BYTE_SIZE);

			PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, ITERATIONS, hash.length * 8);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			byte[] testHash = factory.generateSecret(spec).getEncoded();

			int diff = hash.length ^ testHash.length;
			for (int i = 0; i < hash.length && i < testHash.length; i++) {
				diff |= hash[i] ^ testHash[i];
			}

			return diff == 0;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			return false;
		}
	}
}
