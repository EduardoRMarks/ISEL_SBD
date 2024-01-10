package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoomOccupationUtil {

	// This method retrieves room occupation data for a given date range
	public static List<OccupationData> getOccupationData(Date startDate, Date endDate) {
		List<OccupationData> occupationDataList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DBConnectionManager.getConnection();
			// Assuming you have a table "sala" for rooms and "atividade_sala" for room
			// occupation
			String query = "SELECT sala.Id, clube.DesignacaoComercial, sala.Nome, atividade.IdPt, pt.Nome as PTNOME, atividade.Data, atividade.Estado, atividade.DiaDeSemana, atividade.HoraDeInicio, atividade.Duracao "
					+ "FROM sala " + "LEFT JOIN atividade_sala ON sala.ID = atividade_sala.IdSala "
					+ "LEFT JOIN atividade ON atividade_sala.IdAtividade = atividade.ID "
					+ "LEFT JOIN clube ON sala.NifClube = clube.Nif " + "LEFT JOIN pt ON atividade.IdPt = pt.Id "
					+ "WHERE (atividade.Data BETWEEN ? AND ?) OR (atividade.Tipo = 'Semanal' AND atividade.DiaDeSemana IS NOT NULL AND atividade.HoraDeInicio IS NOT NULL);";

			statement = connection.prepareStatement(query);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			statement.setString(1, dateFormat.format(startDate));
			statement.setString(2, dateFormat.format(endDate));

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int roomId = resultSet.getInt("ID");
				String clubName = resultSet.getString("DesignacaoComercial");
				String roomName = resultSet.getString("Nome");
				int idPt = resultSet.getInt("IdPt");
				String ptNome = resultSet.getString("PTNOME");
				String state = resultSet.getString("Estado");
				String dayOfWeek = resultSet.getString("DiaDeSemana");
				String hour = resultSet.getString("HoraDeInicio");
				int duration = resultSet.getInt("Duracao");

				OccupationData occupationData = new OccupationData(roomId, clubName, roomName, state, ptNome, dayOfWeek,
						hour, idPt, duration);

				// Populate the occupation data for each day in the date range
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(startDate);
				while (!calendar.getTime().after(endDate)) {
					Date currentDate = calendar.getTime();
					boolean isOccupied = resultSet.getInt("ID") != 0
							&& (resultSet.getDate("Data") != null && dateFormat.format(resultSet.getDate("Data"))
									.equals(dateFormat.format(currentDate)))
							|| (dayOfWeek != null && hour != null && checkWeeklyOccupation(currentDate, dayOfWeek));
					occupationData.setRoomOccupied(currentDate, isOccupied);
					calendar.add(Calendar.DAY_OF_WEEK, 1);
				}

				occupationDataList.add(occupationData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.close(resultSet, statement, connection);
		}

		return occupationDataList;
	}

	private static boolean checkWeeklyOccupation(Date currentDate, String dayOfWeek) {
		String[] portugueseDaysOfWeek = { "Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira",
				"Sexta-feira", "Sábado" };

		// Get the day name in Portuguese directly
		String portugueseDayOfWeek = getPortugueseDayOfWeek(currentDate);

		return portugueseDayOfWeek.equalsIgnoreCase(dayOfWeek);
	}

	private static String getPortugueseDayOfWeek(Date currentDate) {
		// Use SimpleDateFormat to get the day name in Portuguese
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", new Locale("pt", "BR"));
		return dateFormat.format(currentDate);
	}

	public static List<OccupationData> getOccupationDataWithCompany(Date startDate, Date endDate, String clube) {
		List<OccupationData> occupationDataList = new ArrayList<>();
		Connection connection = null;

		try {
			connection = DBConnectionManager.getConnection();
			// Assuming you have a table "sala" for rooms and "atividade_sala" for room
			// occupation
			String query = "SELECT sala.Id, clube.DesignacaoComercial, sala.Nome, atividade.IdPt, pt.Nome as ptNome, atividade.Data, atividade.Estado, atividade.DiaDeSemana, atividade.HoraDeInicio, atividade.Duracao "
					+ "FROM sala " + "LEFT JOIN atividade_sala ON sala.ID = atividade_sala.IdSala "
					+ "LEFT JOIN atividade ON atividade_sala.IdAtividade = atividade.ID "
					+ "LEFT JOIN clube ON sala.NifClube = clube.Nif " + "LEFT JOIN pt ON atividade.IdPt = pt.Id "
					+ "WHERE ((atividade.Data BETWEEN ? AND ?) OR (atividade.Tipo = 'Semanal' AND atividade.DiaDeSemana IS NOT NULL AND atividade.HoraDeInicio IS NOT NULL)) "
					+ "AND clube.DesignacaoComercial = ?;";

			try (PreparedStatement statement = connection.prepareStatement(query)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				statement.setString(1, dateFormat.format(startDate));
				statement.setString(2, dateFormat.format(endDate));
				statement.setString(3, clube);

				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						int roomId = resultSet.getInt("ID");
						String clubName = resultSet.getString("DesignacaoComercial");
						String roomName = resultSet.getString("Nome");
						int idPt = resultSet.getInt("IdPt");
						String ptNome = resultSet.getString("ptNome");
						String state = resultSet.getString("Estado");
						String dayOfWeek = resultSet.getString("DiaDeSemana");
						String hour = resultSet.getString("HoraDeInicio");
						int duration = resultSet.getInt("Duracao");

						OccupationData occupationData = new OccupationData(roomId, clubName, roomName, state, ptNome,
								dayOfWeek, hour, idPt, duration);

						// Populate the occupation data for each day in the date range
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(startDate);
						while (!calendar.getTime().after(endDate)) {
							Date currentDate = calendar.getTime();
							boolean isOccupied = resultSet.getInt("ID") != 0
									&& (resultSet.getDate("Data") != null && dateFormat
											.format(resultSet.getDate("Data")).equals(dateFormat.format(currentDate)))
									|| (dayOfWeek != null && hour != null
											&& checkWeeklyOccupation(currentDate, dayOfWeek));
							occupationData.setRoomOccupied(currentDate, isOccupied);
							calendar.add(Calendar.DAY_OF_WEEK, 1);
						}

						occupationDataList.add(occupationData);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.close(null, null, connection);
		}

		return occupationDataList;
	}

	// This inner class represents room occupation data
	public static class OccupationData {
		private int roomId;
		private String clubName;
		private String roomName;
		private int maxOccupation;
		private String state;
		private List<Date> occupiedDates;
		private String ptNome;
		private String dayOfWeek;
		private String hour;
		private int idPt;
		private int duration;

		public OccupationData(int roomId, String clubName, String roomName, String state, String ptNome,
				String dayOfWeek, String hour, int idPt, int duration) {
			this.roomId = roomId;
			this.clubName = clubName;
			this.roomName = roomName;
			this.state = state;
			this.occupiedDates = new ArrayList<>();
			this.ptNome = ptNome;
			this.dayOfWeek = dayOfWeek;
			this.hour = hour;
			this.idPt = idPt;
			this.duration = duration;
		}

		public int getRoomId() {
			return roomId;
		}

		public String getPtNome() {
			return ptNome;
		}

		public String getDayOfWeek() {
			return dayOfWeek;
		}

		public String getHour() {
			return hour;
		}

		public int getIdPt() {
			return idPt;
		}

		public int getDuration() {
			return duration;
		}

		public String getClubName() {
			return clubName;
		}

		public String getRoomName() {
			return roomName;
		}

		public int getMaxOccupation() {
			return maxOccupation;
		}

		public String getState() {
			return state;
		}

		public List<Date> getOccupiedDates() {
			return occupiedDates;
		}

		public void setRoomOccupied(Date date, boolean isOccupied) {
			if (isOccupied) {
				occupiedDates.add(date);
			}
		}

		public boolean isRoomOccupied(Date date) {
			return occupiedDates.contains(date);
		}

		public String toString() {
			return roomId + " " + clubName + " " + roomName + " " + dayOfWeek + " " + hour + " " + duration;
		}
	}
}
