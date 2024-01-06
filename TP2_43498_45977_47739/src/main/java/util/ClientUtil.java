package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public static boolean changeClientInfo(Connection connection, String userNameForm, String userBirthDateForm,
			String userSelfPhoneForm, String userEmailForm, int userNIF, String userEmail, boolean emailChanged) {
		
		String query = "UPDATE `sbd_tp1_43498_45977_47739`.`cliente` SET `Nome` = '"+ userNameForm +"', `DataDeNascimento` = '"+ userBirthDateForm +
    			"', `Telemovel` = '"+ userSelfPhoneForm +"' WHERE (`Nif` = '"+ userNIF +"');";
		
		if(emailChanged) {
			
			//Versão a não usar, mas fica aqui caso se toque no email e assim não dá erro
			String updateQuery = "UPDATE `sbd_tp1_43498_45977_47739`.`cliente` SET `Email` = '"+ userEmailForm +"' WHERE `Email` = '"+ userEmail +"';"
					+ "UPDATE `sbd_tp1_43498_45977_47739`.`utilizador` SET `Email` = '"+ userEmailForm +"' `WHERE Email` = '"+ userEmailForm +"';";
			
			try {
				/*Statement check_statement =  connection.createStatement();
				check_statement.execute("SET FOREIGN_KEY_CHECKS = 0;");
				statement = connection.prepareStatement(updateQuery);
				statement.executeUpdate();
				check_statement.execute("SET FOREIGN_KEY_CHECKS = 1;");*/
				statement = connection.prepareStatement(query);
				statement.executeUpdate();

				return true;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		else {
			try {
				statement = connection.prepareStatement(query);
				statement.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
}
