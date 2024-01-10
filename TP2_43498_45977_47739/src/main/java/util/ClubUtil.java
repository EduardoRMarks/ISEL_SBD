package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClubUtil {

	// This method retrieves the list of club names
	public static List<String> getClubList() {
		List<String> clubList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DBConnectionManager.getConnection();
			// Assuming you have a table "clube" for clubs
			String query = "SELECT DesignacaoComercial FROM clube";

			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String clubName = resultSet.getString("DesignacaoComercial");
				clubList.add(clubName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.close(resultSet, statement, connection);
		}

		return clubList;
	}

	public static int getClubNif(String clubName) {
		int nif = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DBConnectionManager.getConnection();
			String query = "SELECT Nif FROM clube WHERE DesignacaoComercial = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, clubName);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				nif = resultSet.getInt("Nif");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.close(resultSet, statement, connection);
		}

		return nif;
	}
}
