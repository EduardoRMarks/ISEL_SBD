<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Start Menu</title>
<link rel="stylesheet" type="text/css" href="css/start_menu_css.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/spectrum/1.8.1/spectrum.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/spectrum/1.8.1/spectrum.min.js"></script>
</head>
<body>
	<%
	if (request.getAttribute("loginError") != null && (Boolean) request.getAttribute("loginError")) {
	%>
	<script>
		alert("Invalid username or password");
	</script>
	<%
	}
	%>
	<h1>Welcome to the Start Menu</h1>

	<div id="login-form">
		<h2>Login</h2>
		<form method="POST" action="StartMenuServlet">
			<input type="hidden" name="action" value="login"> <label
				for="username">Username:</label> <input type="text" name="username"
				id="username" required><br> <label for="password">Password:</label>
			<input type="password" name="password" id="password" required><br>
			<input type="submit" value="Login">
		</form>
	</div>

	<div id="registration-form">
		<h2>Register</h2>
		<form method="POST" action="StartMenuServlet">
			<input type="hidden" name="action" value="register"> <label
				for="nickname">Nickname:</label> <input type="text" name="nickname"
				required><br> <br> <label for="password">Password:</label>
			<input type="password" name="password" required><br> <br>
			<label for="nationality">Nationality:</label> <select
				name="nationality">
				<%-- Use JSP scriptlet to populate the nationalities dynamically --%>
				<%
				String[] nationalities = java.util.Locale.getISOCountries();
				for (String countryCode : nationalities) {
					java.util.Locale locale = new java.util.Locale("", countryCode);
					String nationality = locale.getDisplayCountry();
				%>
				<option value="<%=countryCode%>"><%=nationality%></option>
				<%
				}
				%>
			</select><br> <br> <label for="age">Age:</label> <input
				type="number" name="age" required><br> <br> <label
				for="picturePath">Picture:</label><input type="text"
				id="picturePath" name="picturePath" required> <label
				for="color">Color:</label> <input type="text" id="colorPicker"
				name="color" required><br> <br> <input
				type="submit" value="Register">
		</form>
	</div>

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
</body>
</html>
