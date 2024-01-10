package tests.util;

import util.PasswordUtil;

public class PasswordUtilManualTest {

	public static void main(String[] args) {

		String password = "nuno";
		String hashedPassword = PasswordUtil.hashPassword(password);

		System.out.println(password);
		System.out.println(hashedPassword);

	}

}
