<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="util.RoomOccupationUtil" %>
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

<h1>Weekly Room Occupation</h1>

<%
    // Initialize variables
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date();

    // Set the calendar to November 1, 2023 (or any other date in November 2023)
    calendar.set(2023, Calendar.NOVEMBER, 1);

    // Use your RoomOccupationUtil to retrieve room occupation data for multiple weeks
    int numberOfWeeks = 4; // Adjust the number of weeks as needed
    for (int week = 0; week < numberOfWeeks; week++) {
        // Calculate the start date of the week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startDate = calendar.getTime();

        // Calculate the end date of the week
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = calendar.getTime();

        // Use your RoomOccupationUtil to retrieve room occupation data for the current week
        List<RoomOccupationUtil.OccupationData> occupationDataList = RoomOccupationUtil.getOccupationData(startDate, endDate);

        // Display the weekly calendar-like view
%>

        <h2>Week starting from <%= dateFormat.format(startDate) %></h2>

        <table>
            <tr>
                <th>Room ID</th>
                <th>Room Name</th>
                <th>Max Occupation</th>
                <th>State</th>
                <th>Occupation</th>
                <!-- Add additional headers for each day of the week -->
                <%
                    Calendar calendarHeader = Calendar.getInstance();
                    calendarHeader.setTime(startDate);
                    for (int i = 0; i < 7; i++) {
                        %>
                        <th><%=dateFormat.format(calendarHeader.getTime())%></th>
                        <%
                        calendarHeader.add(Calendar.DAY_OF_WEEK, 1);
                    }
                %>
            </tr>

            <!-- Iterate over room occupation data and display rows -->
            <%
                for (RoomOccupationUtil.OccupationData occupationData : occupationDataList) {
                    %>
                    <tr>
                        <td><%=occupationData.getRoomId()%></td>
                        <td><%=occupationData.getRoomName()%></td>
                        <td><%=occupationData.getMaxOccupation()%></td>
                        <td><%=occupationData.getState()%></td>
                     
                        <!-- Add additional columns for each day of the week -->
                        <%
                            for (int i = 0; i < 7; i++) {
                                Date currentDateHeader = calendarHeader.getTime();
                                boolean isOccupied = occupationData.isRoomOccupied(currentDateHeader);
                                %>
                                <td><%=isOccupied ? "Occupied" : "Free"%></td>
                                <%
                                calendarHeader.add(Calendar.DAY_OF_WEEK, 1);
                            }
                        %>
                    </tr>
                    <%
                }
            %>
        </table>
<%
        // Move to the next week
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
    }
%>

</body>
</html>
