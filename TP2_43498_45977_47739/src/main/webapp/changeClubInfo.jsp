<%@ page import="java.sql.*"%>
<%@ page import="util.DBConnectionManager"%>
<%@ page import="util.ClubUtil"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
    <title>Change Club Information</title>
</head>
<body>

<%
    Connection connection = null;
    PreparedStatement updateStatement = null;
    PreparedStatement scheduleStatement = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    ResultSet scheduleResultSet = null;

    String action = request.getParameter("action");
    if (action != null && !action.isEmpty()) {
        if (action.equals("updateClubInfo")) {
            String selectedClub = request.getParameter("selectedClub");
            String newPhoneNumber = request.getParameter("newPhoneNumber");

            try {
                connection = DBConnectionManager.getConnection();
                String updateQuery = "UPDATE clube SET Telefone = ? WHERE DesignacaoComercial = ?";

                updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, newPhoneNumber);
                updateStatement.setString(2, selectedClub);
                updateStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (action.equals("updateSchedule")) {
            String selectedClub = request.getParameter("selectedClub");

            // Loop through the days of the week and update the schedule
            for (String dayOfWeek : new String[]{"Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feria", "Sábado", "Domingo"}) {
                String openingTime = request.getParameter("openingTime_" + dayOfWeek);
                String closingTime = request.getParameter("closingTime_" + dayOfWeek);

                try {
                    connection = DBConnectionManager.getConnection();
                    String updateScheduleQuery = "UPDATE horario SET HoraAbertura = ?, HoraFecho = ? WHERE NifClube = ? AND DiaDeSemana = ?";
                    try (PreparedStatement updateScheduleStatement = connection.prepareStatement(updateScheduleQuery)) {
                        // Use Nif from clube table instead of selectedClub
                        int clubeNif = ClubUtil.getClubNif(selectedClub);
                        updateScheduleStatement.setString(1, openingTime);
                        updateScheduleStatement.setString(2, closingTime);
                        updateScheduleStatement.setInt(3, clubeNif);
                        updateScheduleStatement.setString(4, dayOfWeek);
                        updateScheduleStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DBConnectionManager.close2(updateStatement, connection);
                }
            }
        }
    }
%>

<h1>Change Club Information</h1>

<form method="post" action="">
    <label for="selectedClub">Select Club:</label>
    <select name="selectedClub" id="selectedClub">
        <%
            List<String> clubList = ClubUtil.getClubList();
            for (String club : clubList) {
        %>
            <option value="<%= club %>"><%= club %></option>
        <%
            }
        %>
    </select>
    <input type="submit" value="Search"/>
</form>

<%
    String selectedClub = request.getParameter("selectedClub");
    if (selectedClub != null && !selectedClub.isEmpty()) {
        // Retrieve and display current information for the selected club
        try {
            connection = DBConnectionManager.getConnection();
            String query = "SELECT * FROM clube WHERE DesignacaoComercial = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, selectedClub);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
%>
<h2>
    Club Information for
    <%=selectedClub%></h2>
<form method="post" action="">
    <input type="hidden" name="action" value="updateClubInfo"/>
    <input type="hidden" name="selectedClub" value="<%=selectedClub%>"/>
    <label for="newPhoneNumber">New Phone Number:</label>
    <input type="text" name="newPhoneNumber" value="<%=resultSet.getString("Telefone")%>"/><br/>

    <!-- Add fields for other information you want to change -->

    <input type="submit" value="Update"/>
</form>

<h2>
    Club Schedule for
    <%=selectedClub%></h2>
<form method="post" action="">
    <input type="hidden" name="action" value="updateSchedule"/>
    <input type="hidden" name="selectedClub" value="<%=selectedClub%>"/>
    <table border="1">
        <tr>
            <th>Day</th>
            <th>Opening Time</th>
            <th>Closing Time</th>
        </tr>
        <%
            String scheduleQuery = "SELECT * FROM horario WHERE NifClube = ?";
            scheduleStatement = connection.prepareStatement(scheduleQuery);
            scheduleStatement.setString(1, resultSet.getString("Nif"));
            scheduleResultSet = scheduleStatement.executeQuery();
            while (scheduleResultSet.next()) {
        %>
        <tr>
            <td><%=scheduleResultSet.getString("DiaDeSemana")%></td>
            <td><input type="text" name="openingTime_<%=scheduleResultSet.getString("DiaDeSemana")%>"
                       value="<%=scheduleResultSet.getString("HoraAbertura")%>"/></td>
            <td><input type="text" name="closingTime_<%=scheduleResultSet.getString("DiaDeSemana")%>"
                       value="<%=scheduleResultSet.getString("HoraFecho")%>"/></td>
        </tr>
        <%
            }
        %>
    </table>
    <input type="submit" value="Update Schedule"/>
</form>
<%
            } else {
        %>
        <p>Club not found.</p>
        <%
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnectionManager.close(scheduleResultSet, statement, connection);
            DBConnectionManager.closeStatement(scheduleStatement);
        }
    }
%>

</body>
</html>
