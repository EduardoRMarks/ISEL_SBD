<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.Console"%>
<%@page import="util.DBConnectionManager"%>
<%@page import="util.PasswordUtil" %>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@ page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body>

<%

	Date currentDate = new Date();
	
	// Format the date as needed
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String formattedDate = dateFormat.format(currentDate);
    if ("POST".equalsIgnoreCase(request.getMethod())) {
    	
        String username = request.getParameter("username");
        String enteredPassword = request.getParameter("password");

        //Para debug
        //System.out.println("Username: " + username);
        //System.out.println("Password: " + enteredPassword);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionManager.getConnection();

            String query = "SELECT Password, Role FROM utilizador WHERE Email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPasswordFromDatabase = resultSet.getString("Password");
                String userRole = resultSet.getString("Role");

                boolean passwordMatch = PasswordUtil.checkPassword(enteredPassword, hashedPasswordFromDatabase);

                if (passwordMatch) {
                    session.setAttribute("userRole", userRole);
                    request.getSession().setAttribute("userEmail", username);
                    response.sendRedirect(userRole.toLowerCase() + ".jsp");
                    return; 
                } else {
                    out.println("<p style='color: red;'>Incorrect username or password. Please try again.</p>");
                }
            } else {
                out.println("<p style='color: red;'>User not found. Please try again.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnectionManager.close(resultSet, statement, connection);
        }
    }

	session.invalidate();
%>

<div>
	<h1>Bem vindo à aplicação do ginásio Monkey Health Club</h1>
	
	<h3>Data de hoje: <%=formattedDate %></h3>
	
	<form method="post" action="index.jsp">
	    <label for="username">Username:</label>
	    <input type="text" name="username" required/><br/>
	
	    <label for="password">Password:</label>
	    <input type="password" name="password" required/><br/>
	
	    <input type="submit" value="Login"/>
	</form>
</div>

</body>
</html>
