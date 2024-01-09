<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Date" %>
<%@ page import="model.Cliente" %>
<%@ page import="model.UserRole" %>
<%@ page import="util.DBConnectionManager" %>
<%@ page import="util.ClientUtil" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Connection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    Cliente cliente = null;
    String userNIF = null;
    String userName = null;
    Date userBirthDate = null;
    String userSelfPhone = null;
    String userEmail = null;
    // ... other variables

   	List<String> listaPatologias = new ArrayList<String>();
	List<String> listaObjetivos = new ArrayList<String>();
    
    String clientName = request.getParameter("clientName");

    if (clientName != null && !clientName.isEmpty()) {
        try {
            connection = DBConnectionManager.getConnection();
            String query = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Nome = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientName);
            resultSet = preparedStatement.executeQuery();

            // Process the resultSet as needed
            if (resultSet.next()) {
                userNIF = String.valueOf(resultSet.getInt("NIF"));
                userName = resultSet.getString("Nome");
                // ... retrieve other values
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        } finally {
            // Close resources
            DBConnectionManager.close(resultSet, statement, connection);
        }
    } else {
        // Handle the case where clientName is not provided
    }

    try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Nome = '" + clientName + "'";
        
        resultSet = ClientUtil.getClientInfo(connection, query);
        
        userNIF = String.valueOf(resultSet.getInt("NIF"));
        userName = resultSet.getString("Nome");
        userBirthDate = resultSet.getDate("DataDeNascimento");
        userSelfPhone = resultSet.getString("Telemovel");
        userEmail = resultSet.getString("Email");
        
        /*
        System.out.println("NIF: " + userNIF);
        System.out.println("Nome: " + userName);
        System.out.println("Nasc: " + userBirthDate);
        System.out.println("Numero: " + userSelfPhone);
        System.out.println("Email: " + userEmail);
        */
        
        cliente = new Cliente(resultSet.getInt("NIF"), userEmail, UserRole.CLIENT);
        session.setAttribute("user", cliente);
        String client = "client" + userNIF;
        session.setAttribute("client", client);
        
        //Patologias
		query = "SELECT * FROM sbd_tp1_43498_45977_47739.patologia WHERE NifCliente = '" + userNIF + "'";
        listaPatologias = ClientUtil.getPatologiasOrObjetivos(connection, query);
        ArrayList<String> patologias = new ArrayList<>(listaPatologias);
        
        System.out.println("Patologias: " + patologias);
        
        // Objetivos
        query = "SELECT * FROM sbd_tp1_43498_45977_47739.objetivo WHERE NifCliente = '" + userNIF + "'";
        listaObjetivos = ClientUtil.getPatologiasOrObjetivos(connection, query);
        ArrayList<String> objetivos = new ArrayList<>(listaObjetivos);
        
        for (String o : objetivos)
        	System.out.println("Objetivos: " + o);
        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(resultSet, statement, connection);
    }

%>

<div>
    <h1>Pagina do cliente <%= userName %></h1>
    <hr>
    <%
    if(listaObjetivos.size() == 0){
    	%><h2>Atualmente não tem nenhum objetivo definido.</h2><%
    }
    else{
    	for(String o: listaObjetivos) {
        	%>

			<div class="container">
				<h2>Objetivos: </h2>
			    <h3><%= o %></h3>
			</div>
        	<%
		}
    }  
    
    if(listaPatologias.size() == 0){
    	%><h2>Atualmente não tem nenhuma patologia adicionada.</h2><%
    }
    else{
    	for(String p: listaPatologias) {
        	%>

			<div class="container">
				<h2>Patologias: </h2>
			    <h3><%= p %></h3>
			  
			</div>
        	<%
		}
    }   
    %>
    <!-- ... other HTML content -->
</div>

</body>
</html>