<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="util.ClubUtil" %>
<%@ page import="util.EquipmentUtil" %>
<%@ page import="util.RoomUtil" %>
<%@ page import="model.Equipment" %>
<%@ page import="model.Room" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Configure Equipment and Rooms</title>
</head>
<body>

<h1>Configure Equipment and Rooms</h1>

<form method="get">
    Club:
    <select name="selectedClub">
        <option value="">Select a Club</option>
        <% List<String> clubList = ClubUtil.getClubList();
           for (String club : clubList) { %>
            <option value="<%= club %>"><%= club %></option>
        <% } %>
    </select>
    <input type="submit" value="Filter"/>
</form>

<%!String club; %>
<%
    String selectedClub = request.getParameter("selectedClub");
	session.setAttribute("selectedClub", selectedClub);
	int nif = ClubUtil.getClubNif(selectedClub);
	session.setAttribute("nifClub", nif);
	club = selectedClub;
    if (selectedClub != null && !selectedClub.isEmpty()) {

        List<Equipment> equipmentList = EquipmentUtil.getEquipmentList(selectedClub);
        List<Room> roomList = RoomUtil.getRoomList(selectedClub);
%>

    <h2>Equipment</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Demonstration</th>
            <th>Status</th>
            <th>Image</th>
            <th>Action</th>
        </tr>

        <% for (Equipment equipment : equipmentList) { %>
            <tr>
                <td><%= equipment.getId() %></td>
                <td><%= equipment.getNome() %></td>
                <td><%= equipment.getDemonstracao() %></td>
                <td><%= equipment.getEstado() == 1 ? "Active" : "Inactive" %></td>
                <td><img src="data:image/png;base64,<%= equipment.getImagem() %>" alt="Equipment Image"/></td>
                <td>
                    <a href="updateEquipment.jsp?equipmentId=<%= equipment.getId() %>">Update</a>
                    <a href="deleteEquipment.jsp?equipmentId=<%= equipment.getId() %>">Delete</a>
                </td>
            </tr>
        <% } %>
    </table>

    <h2>Rooms</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Max Occupancy</th>
            <th>Status</th>
            <th>Image</th>
            <th>Action</th>
        </tr>

        <% for (Room room : roomList) { %>
            <tr>
                <td><%= room.getId() %></td>
                <td><%= room.getNome() %></td>
                <td><%= room.getOcupacaoMaxima() %></td>
                <td><%= room.getEstado() == 1 ? "Active" : "Inactive" %></td>
                <td><img src="data:image/png;base64,<%= room.getImagem() %>" alt="Room Image"/></td>
                <td>
                    <a href="updateRoom.jsp?roomId=<%= room.getId() %>">Update</a>
                    <a href="deleteRoom.jsp?roomId=<%= room.getId() %>">Delete</a>
                </td>
            </tr>
        <% } %>
    </table>

    <form method="get" action="addEquipment.jsp">
        <input type="hidden" name="selectedClub" value="<%= selectedClub %>"/>
        <input type="submit" value="Add Equipment"/>
    </form>
    
    <form method="get" action="addRoom.jsp">
        <input type="hidden" name="selectedClub" value="<%= selectedClub %>"/>
        <input type="submit" value="Add Room"/>
    </form>

<%
    }
%>

<button onclick="goBack()">Voltar</button>

<script>
function goBack() {
	window.location.href = "administrador.jsp";
}
</script>

</body>
</html>
