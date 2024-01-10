<%@page import="util.ClientUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@page import="util.DBConnectionManager"%>
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
	%>
    <h1>Criar/Gerir Perfis de Utilizadores</h1>

    <h2>Lista de Utilizadores</h2>
    <table border="1">
        <tr>
            <th>Email</th>
            <th>Role</th>
        </tr>
        <% 
            try {
                connection = DBConnectionManager.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rsUtilizadores = stmt.executeQuery("SELECT * FROM sbd_tp1_43498_45977_47739.utilizador;");

                while (rsUtilizadores.next()) {
        %>
                    <tr>
                        <td><%= rsUtilizadores.getString("Email") %></td>
                        <td><%= rsUtilizadores.getString("Role") %></td>
                    </tr>
        <%
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        %>
    </table>

	<h2>Importar/Exportar Perfis com XML</h2>
	<form action="XmlServlet" method="post" enctype="multipart/form-data">
	    <label for="xmlFile">Escolha um ficheiro XML:</label>
	    <input type="file" name="xmlFile" accept=".xml" required><br>
	    <input type="submit" value="Importar Perfis">
	</form>
	
	<form action="XmlServlet" method="post" enctype="multipart/form-data">
	    <input type="hidden" name="export" value="true">
	    <label for="exportUserID">Escolha o userID para exportar:</label>
	    <input type="text" name="UserEmail" required><br>	
	    <input type="submit" value="Exportar Perfis">
	</form>
	
	<h1>Criar Clientes</h1>
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