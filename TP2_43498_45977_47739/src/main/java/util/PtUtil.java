package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PtUtil {

	static PreparedStatement statement = null;
	static ResultSet resultSet = null;

	public static ResultSet getPtInfo(Connection connection, String query) throws SQLException {
		statement = connection.prepareStatement(query);
		resultSet = statement.executeQuery();

		if (resultSet.next()) {
			return resultSet; } 
		else { return null; }
	}

    public static List<String> getClientes(Connection connection, String query) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> clientesStrings = new ArrayList<String>();

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                clientesStrings.add(resultSet.getString("Nome")); }
        } 
        finally {

            if (resultSet != null) { resultSet.close(); }
            if (statement != null) { statement.close(); }
        }

        return clientesStrings;
    }

}