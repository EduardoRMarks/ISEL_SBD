<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Administrator Dashboard</title>
</head>
<body>

<button onclick="Logout()">LogOut</button>

<h1>Welcome, Administrator!</h1>

<div style="text-align: center; margin-top: 20px;">
    <form action="changeClubInfo.jsp">
        <input type="submit" value="Change Club Info">
    </form>

    <form action="configureEquipmentAndRooms.jsp">
        <input type="submit" value="Configure Equipment And Rooms">
    </form>

    <form action="createUserProfiles_xml.jsp">
        <input type="submit" value="Create User Profiles">
    </form>

    <form action="see_room_usage.jsp">
        <input type="submit" value="See Weekly Room Usage">
    </form>
</div>

<script>
function Logout() {
	window.location.href = "index.jsp";
}
</script>

</body>
</html>
