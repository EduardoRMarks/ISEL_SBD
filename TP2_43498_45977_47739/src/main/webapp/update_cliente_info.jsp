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
<%@ page import="java.text.SimpleDateFormat" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	User user = null;
	
	int userNIF = 0;
	String userName = null;
	Date userBirthDate = null;
	String userSelfPhone = null;
	String userEmail = null;
	
	boolean updateResult = false;
	
	user = (User) session.getAttribute("user");
	userEmail = user.getUserEmail();
	//userEmail = "eduardo@aol.com";
	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Email = '" + userEmail + "'";
        
        resultSet = ClientUtil.getClientInfo(connection, query);
        
        userNIF = resultSet.getInt("NIF");
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
			
            String userNameForm = request.getParameter("nome");
            String userBirthDateForm = request.getParameter("DataDeNascimento");
            String userSelfPhoneForm = request.getParameter("Telemovel");
            
            if(!userNameForm.equals(userName) || !userBirthDateForm.equals(dateFormat.format(userBirthDate)) 
            		|| !userSelfPhoneForm.equals(userSelfPhone)){
            	
            	updateResult = ClientUtil.changeClientInfo(connection, userNameForm,
            			userBirthDateForm, userSelfPhoneForm, userNIF);
            }          
            
            response.sendRedirect("cliente.jsp");

		} catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBConnectionManager.close(resultSet, statement, connection);
	    }
	}
	
//""
%>
	<div>
	<form id="updatePerfil" method="post" action="update_cliente_info.jsp" class="form-container">
    	
		<label for="nome">Nome:</label>
    	<input type="text" name="nome" value="<%= userName %>" required/><br/>
    	
    	<label for="DataDeNascimento">Data de Nascimento:</label>
    	<input type="date" name="DataDeNascimento" value="<%= userBirthDate %>" required/><br/>
    	
    	<label for="Telemovel">Telemovel:</label>
    	<input type="text" name="Telemovel" value="<%= userSelfPhone %>" required/><br/>
    	
        <input type="submit" value="update"/>
    </form>
    </div>
    
    <button onclick="redirectToPage('cliente.jsp')">Voltar</button>
	
	<script>
	    function redirectToPage(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>
</body>
</html>