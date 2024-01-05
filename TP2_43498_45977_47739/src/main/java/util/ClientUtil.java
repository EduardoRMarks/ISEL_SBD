package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientUtil {
	
	 static PreparedStatement statement = null;
	 static ResultSet resultSet = null;
	
	public static ResultSet getClientInfo(Connection connection, String query) throws SQLException {
		statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
        	System.out.println(resultSet.getInt("NIF"));
        	return resultSet;
        }
        else {
			return null;
		}
	}
	
}
