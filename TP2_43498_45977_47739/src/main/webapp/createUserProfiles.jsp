<%@page import="util.ClientUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="util.DBConnectionManager" %>
<html>
<head>
    <title>Criar/Gerir Perfis de Utilizadores</title>
   
</head>
<body>
 <%
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	
	int userNIF = 0;
	String userName = null;
	Date userBirthDate = null;
	String userSelfPhone = null;
	String userEmail = null;
	
	boolean updateResult = false;
	
	if ("POST".equalsIgnoreCase(request.getMethod())) {
		try{
			connection = DBConnectionManager.getConnection();
			
            String userNameForm = request.getParameter("Nome");
            String userNIfForm = request.getParameter("NIF");
            String userBirthDateForm = request.getParameter("DataDeNascimento");
            String userSelfPhoneForm = request.getParameter("Telemovel");
            String userEmailForm = request.getParameter("Email");
            String userPasswordForm = request.getParameter("Password");
            
            
            ClientUtil.createCliente(connection, userNameForm, userNIfForm, userBirthDateForm, userSelfPhoneForm, userEmailForm, userPasswordForm);
            
            response.sendRedirect("administrador.jsp");

		} catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBConnectionManager.close(resultSet, statement, connection);
	    }
	}
	
//""
%>
	<div>
	<form id="updatePerfil" method="post" action="createUserProfiles.jsp" class="form-container">
    	
		<label for=Nome>Nome:</label>
    	<input type="text" name="Nome" required/><br/>
    	
    	<label for=NIF>Nif:</label>
    	<input type="number" name="NIF" required/><br/>
    	
    	<label for="DataDeNascimento">Data de Nascimento:</label>
    	<input type="date" name="DataDeNascimento" required/><br/>
    	
    	<label for="Telemovel">Telemovel:</label>
    	<input type="text" name="Telemovel" value="+351 " required/><br/>
    	
    	<label for="Email">Email:</label>
    	<input type="text" name="Email" required/><br/>
    	
    	<label for="Password">Password:</label>
    	<input type="password" name="Password" required/><br/>
    	
        <input type="submit" value="Criar"/>
    </form>
    </div>
    
    <button onclick="redirectToPage('administrador.jsp')">Voltar</button>
	
	<script>
	    function redirectToPage(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>
    
</body>
</html>
</html>