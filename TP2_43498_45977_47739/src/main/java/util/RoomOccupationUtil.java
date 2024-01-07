package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoomOccupationUtil {
    // This method retrieves room occupation data for a given date range
    public static List<OccupationData> getOccupationData(Date startDate, Date endDate) {
        List<OccupationData> occupationDataList = new ArrayList<>();

        try (Connection connection = DBConnectionManager.getConnection()) {
            // Assuming you have a table "sala" for rooms and "atividade_sala" for room occupation
            String query = "SELECT sala.Id, sala.Nome, sala.OcupacaoMaxima, sala.Estado, atividade_sala.IdSala, atividade.Data\n"
            		+ "FROM sala\n"
            		+ "LEFT JOIN atividade_sala ON sala.ID = atividade_sala.IdSala\n"
            		+ "LEFT JOIN atividade ON atividade_sala.IdAtividade = atividade.ID\n"
            		+ "WHERE atividade.Data BETWEEN '2023-11-01' AND '2023-11-30';";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                statement.setString(1, dateFormat.format(startDate));
                statement.setString(2, dateFormat.format(endDate));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int roomId = resultSet.getInt("ID");
                        String clubName = resultSet.getString("Clube");
                        String roomName = resultSet.getString("Name");
                        int maxOccupation = resultSet.getInt("MaxOcupation");
                        int state = resultSet.getInt("State");

                        OccupationData occupationData = new OccupationData(roomId, clubName, roomName, maxOccupation, state);

                        // Populate the occupation data for each day in the date range
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);
                        while (!calendar.getTime().after(endDate)) {
                            Date currentDate = calendar.getTime();
                            boolean isOccupied = resultSet.getInt("IdSala") != 0 &&
                                                 dateFormat.format(resultSet.getDate("Data")).equals(dateFormat.format(currentDate));
                            occupationData.setRoomOccupied(currentDate, isOccupied);
                            calendar.add(Calendar.DAY_OF_WEEK, 1);
                        }

                        occupationDataList.add(occupationData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return occupationDataList;
    }

    // This inner class represents room occupation data
    public static class OccupationData {
        private int roomId;
        private String clubName;
        private String roomName;
        private int maxOccupation;
        private int state;
        private List<Date> occupiedDates;

        public OccupationData(int roomId, String clubName, String roomName, int maxOccupation, int state) {
            this.roomId = roomId;
            this.clubName = clubName;
            this.roomName = roomName;
            this.maxOccupation = maxOccupation;
            this.state = state;
            this.occupiedDates = new ArrayList<>();
        }

        public int getRoomId() {
            return roomId;
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

        public int getState() {
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
    }
}
