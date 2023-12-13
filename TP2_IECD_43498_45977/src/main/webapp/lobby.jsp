<%@ page import="java.util.List"%>
<%@ page import="java.util.Collections"%>
<%@ page import="TP1.User"%>
<%@ page import="TP1.UserXmlWriter"%>
<%@ page import="TP2.NationalityUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/gh/lipis/flag-icons@6.6.6/css/flag-icons.min.css" />
<link rel="stylesheet" type="text/css" href="css/lobby_css.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/spectrum/1.8.1/spectrum.min.css">
<meta charset="UTF-8">
<title>Lobby</title>

    <style>
        <% User user2 = (User) session.getAttribute("user"); 
        String userColor = user2.getColor();%>
        body {
            background-color: <%= userColor != null ? userColor : "#ffffff" %>;
        }
    </style>
</head>
<body>
	<h1>Welcome to the Lobby</h1>

	<h2>User Information</h2>
	<p>
		<strong>Nickname:</strong> ${nickname}
	</p>
	<p>
		<strong>Nationality:</strong> ${nationality}
	</p>
	<p>
		<strong>Age:</strong> ${age}
	</p>

	<p>
		<strong>Picture Path:</strong> ${picture}
	</p>

	<h2>Update Information</h2>
	<form action="LobbyServlet" method="post">
		<label for="nickname">Nickname:</label> <input type="text"
			id="nickname" name="nickname"> <br> <br> <label
			for="password">Password:</label> <input type="text" id="password"
			name="password"> <br> <br> <label for="nationality">Nationality:</label>
		<select name="nationality" id="nationality"></select> <br> <br>
		<label for="age">Age:</label> <input type="text" id="age" name="age">
		<label for="picture">Picture Path:</label> <input type="text"
			id="picture" name="picture"> <br> <br> <label
			for="color">Color:</label> <input type="text" id="colorPicker"
			name="color"><br> <br> <input type="submit"
			value="Update">
	</form>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/spectrum/1.8.1/spectrum.min.js"></script>
	<script>
		// Initialize Spectrum Color Picker
		$(document).ready(
				function() {
					$('#colorPicker').spectrum(
							{
								preferredFormat : "hex",
								showInput : true,
								showPalette : true,
								palette : [
										[ "#ff0000", "#00ff00", "#0000ff" ],
										[ "#ffff00", "#ff00ff", "#00ffff" ]
								// Add more colors to the palette as needed
								],
								container : "#colorPickerContainer"
							});
				});
	</script>

	<h2>Start a Game</h2>
	<button class="start-game-btn"
		onclick="location.href='FourInARowServlet'">Start a Game</button>


	<h2>Ranking</h2>
	<table>
		<tr>
			<th>Rank</th>
			<th>Nickname</th>
			<th>Nationality</th>
			<th>Wins</th>
			<th>Time</th>
		</tr>

		<%
		UserXmlWriter userXmlWriter = (UserXmlWriter) session.getAttribute("userXmlWriter");
		List<User> users = userXmlWriter.readUsersFromFile();

		// Sort the users based on wins and time
		Collections.sort(users, (u1, u2) -> {
			if (u1.getWins() != u2.getWins()) {
				return Integer.compare(Integer.parseInt(u2.getWins()), Integer.parseInt(u1.getWins())); // Sort by wins in descending order
			} else {
				return u1.getTime().compareTo(u2.getTime()); // Sort by time in ascending order
			}
		});
		%>

		<%-- Iterate over the sorted users and display their information --%>
		<%!int rank = 1;%>
		<%
		for (User user : users) {
		%>
		<tr>
			<td><%=rank%></td>
			<td><%=user.getNickname()%></td>
			<td><span
				class="fi fi-<%=NationalityUtils.convertToNationalityCode(user.getNationality()).toLowerCase()%> fis"></span></td>
			<td><%=user.getWins()%></td>
			<td><%=user.getTime()%></td>
		</tr>
		<%
		rank++;
		%>
		<%
		}
		%>
	</table>

	<script>
    // Fetch the nationalities data from the API
    fetch('https://restcountries.com/v3.1/all')
      .then(response => response.json())
      .then(data => {
        const selectElement = document.getElementById('nationality');

        // Create the first option as null
        const nullOption = document.createElement('option');
        nullOption.value = "";
        nullOption.textContent = "";
        selectElement.appendChild(nullOption);

        // Iterate over the nationalities data and create options
        data.forEach(nationality => {
          const optionElement = document.createElement('option');
          optionElement.value = nationality.name.common;
          optionElement.textContent = nationality.name.common;
          selectElement.appendChild(optionElement);
        });
      })
      .catch(error => {
        console.error('Error fetching nationalities:', error);
      });
  </script>
</body>
</html>
