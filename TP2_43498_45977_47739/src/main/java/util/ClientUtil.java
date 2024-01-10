package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientUtil {

	static PreparedStatement statement = null;
	static ResultSet resultSet = null;

	public static boolean createCliente(Connection connection, String nome, String nif, String birthdate,
			String selfPhone, String email, String password) {

		String hashedPassword = PasswordUtil.hashPassword(password);

		try {
			String query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role)" + " VALUES ('"
					+ email + "', '" + hashedPassword + "', 'Cliente');";
			statement = connection.prepareStatement(query);
			statement.executeUpdate();

			query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`, `Nome`, `DataDeNascimento`, "
					+ "`Telemovel`, `Email`) VALUES ('" + nif + "', '" + nome + "', '" + birthdate + "', '" + selfPhone
					+ "', '" + email + "');";
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static ResultSet getClientInfo(Connection connection, String query) throws SQLException {
		statement = connection.prepareStatement(query);
		resultSet = statement.executeQuery();

		if (resultSet.next()) {
			return resultSet;
		} else {
			return null;
		}
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

	public static boolean changePassword(Connection connection, String userEmail, String password) {

		String hashedPassword = PasswordUtil.hashPassword(password);

		String query = "UPDATE `sbd_tp1_43498_45977_47739`.`utilizador` SET `Password` = '" + hashedPassword
				+ "' WHERE (`Email` = '" + userEmail + "');";
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

	public static List<String> getRecomendacoes(Connection connection, String query) throws SQLException {

		statement = connection.prepareStatement(query);
		resultSet = statement.executeQuery();
		List<String> recomendacaoStrings = new ArrayList<String>();

		while (resultSet.next()) {
			recomendacaoStrings.add(resultSet.getString("IdPt"));
			recomendacaoStrings.add(resultSet.getString("IdEquipamento"));
			recomendacaoStrings.add(resultSet.getString("Data"));
			recomendacaoStrings.add(resultSet.getString("Uso"));
		}

		return recomendacaoStrings;
	}

	public static boolean addOrDelete(Connection connection, String which_table, String action,
			String patologia_objetivo, String nif) {

		if (action.equals("adicionar")) {
			String query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`" + which_table
					+ "` (`Nome`, `NifCliente`) VALUES ('" + patologia_objetivo + "', '" + nif + "');";
			try {
				statement = connection.prepareStatement(query);
				statement.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		if (action.equals("eliminar")) {
			String query = "DELETE FROM `sbd_tp1_43498_45977_47739`.`" + which_table + "` WHERE (`Nome` = '"
					+ patologia_objetivo + "') and (`NifCliente` = '" + nif + "');";
			try {
				statement = connection.prepareStatement(query);
				statement.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

		return false;
	}

	public static boolean inscrito(Connection connection, String idAtividade, int nifCliente) {
		String query = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente_atividade WHERE IdAtividade = '" + idAtividade
				+ "' AND NifCliente = '" + nifCliente + "';";
		try {
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean inscrever(Connection connection, String tipoAtividade, String idAtividade, String userNif,
			String idPt) {
		String query = null;
		if (tipoAtividade.equals("individuais")) {
			try {
				query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente_atividade` "
						+ "(`NifCliente`, `IdAtividade`, `IdPt`) VALUES ('" + userNif + "', '" + idAtividade + "', '"
						+ idPt + "');";
				statement = connection.prepareStatement(query);
				statement.executeUpdate();

				query = "UPDATE `sbd_tp1_43498_45977_47739`.`atividade` SET `Estado` = 'InscricoesFechadas' "
						+ "WHERE (`Id` = '" + idAtividade + "') and (`IdPt` = '" + idPt + "');";
				statement = connection.prepareStatement(query);
				statement.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try {
				query = "SELECT MaxParticipantes FROM sbd_tp1_43498_45977_47739.atividade WHERE Id='" + idAtividade
						+ "';";
				statement = connection.prepareStatement(query);
				resultSet = statement.executeQuery();
				int maxInscricoes = 0;

				if (resultSet.next()) {
					maxInscricoes = resultSet.getInt("MaxParticipantes");
				}

				int numeroInscritos = 0;

				query = "SELECT COUNT(*) AS user_count FROM sbd_tp1_43498_45977_47739.cliente_atividade WHERE IdAtividade='"
						+ idAtividade + "'";

				statement = connection.prepareStatement(query);
				ResultSet countResultSet = statement.executeQuery();

				if (countResultSet.next()) {
					numeroInscritos = countResultSet.getInt("user_count");
				}

				query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente_atividade` "
						+ "(`NifCliente`, `IdAtividade`, `IdPt`) VALUES ('" + userNif + "', '" + idAtividade + "', '"
						+ idPt + "');";
				statement = connection.prepareStatement(query);
				statement.executeUpdate();

				numeroInscritos++;
				if (numeroInscritos == maxInscricoes) {
					query = "UPDATE `sbd_tp1_43498_45977_47739`.`atividade` SET `Estado` = 'InscricoesFechadas' "
							+ "WHERE (`Id` = '" + idAtividade + "') and (`IdPt` = '" + idPt + "');";
					statement = connection.prepareStatement(query);
					statement.executeUpdate();
				}

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	public static boolean cancelar(Connection connection, String tipoAtividade, String idAtividade, String userNif,
			String idPt) {
		String query = null;
		if (tipoAtividade.equals("individuais")) {
			try {
				query = "DELETE FROM `sbd_tp1_43498_45977_47739`.`cliente_atividade`" + " WHERE NifCliente = '"
						+ userNif + "' and IdAtividade = '" + idAtividade + "' and IdPt = '" + idPt + "';";
				statement = connection.prepareStatement(query);
				statement.executeUpdate();

				query = "UPDATE `sbd_tp1_43498_45977_47739`.`atividade` SET `Estado` = 'InscricoesAbertas' "
						+ "WHERE (`Id` = '" + idAtividade + "') and (`IdPt` = '" + idPt + "');";
				statement = connection.prepareStatement(query);
				statement.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try {
				query = "SELECT MaxParticipantes FROM sbd_tp1_43498_45977_47739.atividade WHERE Id='" + idAtividade
						+ "';";
				statement = connection.prepareStatement(query);
				resultSet = statement.executeQuery();
				int maxInscricoes = 0;

				if (resultSet.next()) {
					maxInscricoes = resultSet.getInt("MaxParticipantes");
				}

				int numeroInscritos = 0;

				query = "SELECT COUNT(*) AS user_count FROM sbd_tp1_43498_45977_47739.cliente_atividade WHERE IdAtividade='"
						+ idAtividade + "'";

				statement = connection.prepareStatement(query);
				ResultSet countResultSet = statement.executeQuery();

				if (countResultSet.next()) {
					numeroInscritos = countResultSet.getInt("user_count");
				}

				query = "DELETE FROM `sbd_tp1_43498_45977_47739`.`cliente_atividade`" + " WHERE NifCliente = '"
						+ userNif + "' and IdAtividade = '" + idAtividade + "' and IdPt = '" + idPt + "';";
				statement = connection.prepareStatement(query);
				statement.executeUpdate();

				numeroInscritos--;
				if (numeroInscritos == maxInscricoes - 1) {
					query = "UPDATE `sbd_tp1_43498_45977_47739`.`atividade` SET `Estado` = 'InscricoesAbertas' "
							+ "WHERE (`Id` = '" + idAtividade + "') and (`IdPt` = '" + idPt + "');";
					statement = connection.prepareStatement(query);
					statement.executeUpdate();
				}

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	public static String getDayOfWeekString(int dayOfWeek) {
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return "Domingo";
		case Calendar.MONDAY:
			return "Segunda-Feira";
		case Calendar.TUESDAY:
			return "Terça-Feira";
		case Calendar.WEDNESDAY:
			return "Quarta-Feira";
		case Calendar.THURSDAY:
			return "Quinta-Feira";
		case Calendar.FRIDAY:
			return "Sexta-Feira";
		case Calendar.SATURDAY:
			return "Sábado";
		default:
			return "Invalid day of the week";
		}
	}
}
