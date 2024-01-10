<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Administrator Dashboard</title>
</head>
<body>

<h1>Welcome, Administrator!</h1>

<div style="text-align: center; margin-top: 20px;">
    <!-- Button to change work hours of a gym -->
    <form action="changeClubInfo.jsp">
        <input type="submit" value="Change Club Info">
    </form>

    <!-- Button to configure equipment in a gym -->
    <form action="configureEquipmentAndRooms.jsp">
        <input type="submit" value="Configure Equipment And Rooms">
    </form>

    <!-- Button to create user profiles -->
    <form action="createUserProfiles.jsp">
        <input type="submit" value="Create User Profiles">
    </form>

    <!-- Button to see top 5 equipment usage -->
    <form action="top5EquipmentUsage.jsp">
        <input type="submit" value="See Top 5 Equipment Usage">
    </form>
    
        <!-- Button to create user profiles -->
    <form action="see_room_usage.jsp">
        <input type="submit" value="See Weekly Room Usage">
    </form>
</div>

</body>
</html>
