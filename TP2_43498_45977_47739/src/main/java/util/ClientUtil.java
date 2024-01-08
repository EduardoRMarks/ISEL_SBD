package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientUtil {

	static PreparedStatement statement = null;
	static ResultSet resultSet = null;

	public static ResultSet getClientInfo(Connection connection, String query) throws SQLException {
		statement = connection.prepareStatement(query);
		resultSet = statement.executeQuery();

		if (resultSet.next()) {
			return resultSet; } 
		else { return null; }
	}

	public static boolean changeClientInfo(Connection connection, String userNameForm, String userBirthDateForm,
			String userSelfPhoneForm, int userNIF) {

		String query = "UPDATE `sbd_tp1_43498_45977_47739`.`cliente` SET `Nome` = '" + userNameForm
				+ "', `DataDeNascimento` = '" + userBirthDateForm + "', `Telemovel` = '" + userSelfPhoneForm
				+ "' WHERE (`Nif` = '" + userNIF + "');";

		try {
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<String> getPatologiasOrObjetivos(Connection connection, String query) throws SQLException {
		statement = connection.prepareStatement(query);
		resultSet = statement.executeQuery();
		List<String> patologiaStrings = new ArrayList<String>();

		while (resultSet.next()) {
			patologiaStrings.add(resultSet.getString("Nome"));
		}

		return patologiaStrings;
	}

}
