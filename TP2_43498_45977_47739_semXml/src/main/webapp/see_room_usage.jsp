<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="util.RoomOccupationUtil" %>
<%@ page import="util.ClubUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<!DOCTYPE html>
<html>
<head>
    <title>Weekly Room Occupation</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>

<button onclick="goBack()">Voltar</button>

<h1>Weekly Room Occupation</h1>

<%
    // Initialize variables
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Check if a specific week is requested
    String action = request.getParameter("action");

    // Retrieve or initialize the start date of the week from the session
    Date startDate = (Date) session.getAttribute("startDate");
    if (startDate == null || "filter".equals(action)) {
        // If not in session or filter action, set the start date to the current week's start date
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        startDate = calendar.getTime();
        session.setAttribute("startDate", startDate);
    }

    // Adjust the start date based on the button pressed
    if ("previous".equals(action)) {
        // Move to the previous week
        calendar.setTime(startDate);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        startDate = calendar.getTime();
        session.setAttribute("startDate", startDate);
    } else if ("next".equals(action)) {
        // Move to the next week
        calendar.setTime(startDate);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        startDate = calendar.getTime();
        session.setAttribute("startDate", startDate);
    }

    // Calculate the end date of the week
    calendar.setTime(startDate);
    calendar.add(Calendar.DAY_OF_WEEK, 6);
    Date endDate = calendar.getTime();

    String selectedClub = request.getParameter("selectedClub");
    List<RoomOccupationUtil.OccupationData> occupationDataList;
    if ("filter".equals(action) && selectedClub != null && !selectedClub.isEmpty()) {
        occupationDataList = RoomOccupationUtil.getOccupationDataWithCompany(startDate, endDate, selectedClub);
    } else {
        occupationDataList = RoomOccupationUtil.getOccupationData(startDate, endDate);
    }

    // Get the list of clubs for the dropdown
    List<String> clubList = ClubUtil.getClubList();
%>

<form method="get">
    Club: 
    <select name="selectedClub">
        <option value="">All Clubs</option>
        <% for (String club : clubList) { %>
            <option value="<%= club %>" <%= selectedClub != null && selectedClub.equals(club) ? "selected" : "" %>><%= club %></option>
        <% } %>
    </select>
    <input type="submit" value="Filter" name="action"/>
</form>

<form method="get">
    <input type="hidden" name="action" value="previous"/>
    <input type="hidden" name="selectedClub" value="<%= selectedClub %>"/>
    <input type="submit" value="Previous Week"/>
</form>

<form method="get">
    <input type="hidden" name="action" value="next"/>
    <input type="hidden" name="selectedClub" value="<%= selectedClub %>"/>
    <input type="submit" value="Next Week"/>
</form>

<h2>Week starting from <%= dateFormat.format(startDate) %></h2>

<%
    // Iterate through the dates
    calendar.setTime(startDate);
    while (!calendar.getTime().after(endDate)) {
        Date currentDate = calendar.getTime();
%>

        <h3><%= dateFormat.format(currentDate) %></h3>

        <table>
            <tr>
                <th>Room ID</th>
                <th>Club Name</th>
                <th>Room Name</th>
                <th>PT ID</th>
                <th>PT Name</th>
                <th>State</th>
                <th>Start Time</th>
                <th>Duration</th>
            </tr>

            <!-- Iterate over room occupation data and display rows for the current date -->
            <%
                for (RoomOccupationUtil.OccupationData occupationData : occupationDataList) {
                	
                    // Check if the room is occupied on the current date
                    boolean isOccupied = occupationData.isRoomOccupied(currentDate);
                    if (isOccupied) {
                %>
                    <tr>
                        <td><%=occupationData.getRoomId()%></td>
                        <td><%=occupationData.getClubName()%></td>
                        <td><%=occupationData.getRoomName()%></td>
                        <td><%=occupationData.getIdPt() %></td>
                        <td><%=occupationData.getPtNome() %></td>
                        <td><%=occupationData.getState()%></td>
                        <td><%=occupationData.getHour()%></td>
                        <td><%=occupationData.getDuration() %></td>
                    </tr>
                <%
                    }
                }
            %>
        </table>

<%
        // Move to the next date
        calendar.add(Calendar.DAY_OF_WEEK, 1);
    }
%>

<script>
function goBack() {
	window.location.href = "administrador.jsp";
}
</script>

</body>
</html>
