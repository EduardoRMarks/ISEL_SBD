<%@page import="java.sql.Date"%>
<%@page import="java.io.Console"%>
<%@page import="model.User" %>
<%@page import="model.UserRole" %>
<%@page import="util.DBConnectionManager"%>
<%@page import="util.ClientUtil"%>
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
    <title>Client Page</title>
    <style>
        .form-container {
            display: none;
        }
    </style>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	User user = null;
	
	String userNIF = null;
	String userName = null;
	Date userBirthDate = null;
	String userSelfPhone = null;
	
	String userEmail = (String) session.getAttribute("userEmail");
	//System.out.println(userName);
	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Email = '" + userEmail + "'";
        
        resultSet = ClientUtil.getClientInfo(connection, query);
        
        userNIF = String.valueOf(resultSet.getInt("NIF"));
        userName = resultSet.getString("Nome");
        userBirthDate = resultSet.getDate("DataDeNascimento");
        userSelfPhone = resultSet.getString("Telemovel");
        
        user = new User(resultSet.getInt("NIF"), userEmail, UserRole.CLIENT);
        session.setAttribute("user", user);
        String client = "client" + userNIF;
        session.setAttribute("client", client);
        
        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(resultSet, statement, connection);
    }
	
	if ("POST".equalsIgnoreCase(request.getMethod())) {
		try{
			connection = DBConnectionManager.getConnection();
			
			String formId = request.getParameter("updatePerfil");
			System.out.println(formId);
			
			String username = request.getParameter("username");
	        String enteredPassword = request.getParameter("password");
		} catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBConnectionManager.close(resultSet, statement, connection);
	    }
	}
%>

<div>
	<h1><%= user.toString() %></h1>
	<h3>Bem vindo <%= userName %></h3>
	<button onclick="showForm('updatePerfil')">Atualizar o perfil</button>
    <button onclick="showForm('form2')">Show Form 2</button>
    <button onclick="showForm('form3')">Show Form 3</button>
    <button onclick="showForm('')">Hide</button>
    
    <form action="update_cliente_info.jsp" method="get">
        <input type="submit" value="Update Info">
    </form>
    
    <form action="update_patologias.jsp" method="get">
        <input type="submit" value="Update Info">
    </form>
    
    <form action="update_objeyivos.jsp" method="get">
        <input type="submit" value="Update Info">
    </form>

    

    <form id="form2" method="post" action="cliente.jsp" class="form-container">
        <!-- Form 2 content goes here -->
        <label for="input2">Input 2:</label>
        <input type="text" id="input2" name="input2">
    </form>

    <form id="form3" method="post" action="cliente.jsp" class="form-container">
        <!-- Form 3 content goes here -->
        <label for="input3">Input 3:</label>
        <input type="text" id="input3" name="input3">
    </form>

    <script>
        function showForm(formId) {
            // Hide all forms
            var forms = document.getElementsByClassName('form-container');
            for (var i = 0; i < forms.length; i++) {
                forms[i].style.display = 'none';
            }

            // Show the selected form
            var selectedForm = document.getElementById(formId);
            if (selectedForm) {
                selectedForm.style.display = 'block';
            }
        }
    </script>
</div>

</body>
</html>