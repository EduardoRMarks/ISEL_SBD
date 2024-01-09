<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Date" %>
<%@ page import="model.Cliente" %>
<%@ page import="model.UserRole" %>
<%@ page import="model.Pt" %>
<%@ page import="util.PtUtil"%>
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
    <title>Recomendar clientes Page</title>
</head>
<body>

<%
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Cliente cliente = null;
    
    String nifCliente = null;
    String idEquipamento = null;
    String nifClube = null;
    Date data = null;
    String uso = null;
    // ... other variables

   	List<String> listaNifClientes = new ArrayList<String>();
   	ArrayList<String> listaClientes = new ArrayList<String>();
   	List<String> equipamentosDisponiveis = new ArrayList<String>();
   	List<String> listaClientesFinal = new ArrayList<String>();
   	
   	String idPT = (String) session.getAttribute("idPT");
   	String nomePt = (String) session.getAttribute("nomePt");
   	
	System.out.println("IDPT: " + idPT);
	System.out.println("nome pt: " + nomePt);

    try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.pt_cliente_equipamento WHERE IdPt = '" + idPT + "'";

        resultSet = PtUtil.getPtInfo(connection, query);
        listaNifClientes = PtUtil.getNifClientes(connection, query);
        
        nifCliente = String.valueOf(resultSet.getInt("NifCliente"));
        idEquipamento = resultSet.getString("IdEquipamento");
        nifClube = resultSet.getString("NifClube");
        data = resultSet.getDate("Data");
        uso = resultSet.getString("Uso");
        
        for (String s : listaNifClientes) {
        	String queryClientes = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Nif = '" + s + "'";
        	listaClientes.add(PtUtil.getNomeCliente(connection, queryClientes));
        }
        

        Set<String> setWithoutDuplicates = new LinkedHashSet<>(listaClientes);
        listaClientesFinal = new ArrayList<>(setWithoutDuplicates);

        // Fetch available equipment from the database
        query = "SELECT Nome FROM sbd_tp1_43498_45977_47739.equipamento WHERE Estado = 1";
        equipamentosDisponiveis = PtUtil.getEquipamentosDisponiveis(connection, query);
        
        for (String equipment : equipamentosDisponiveis) {
            System.out.println("Equipment: " + equipment); // Output equipment names for debugging
        }
        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(resultSet, statement, connection);
    }

%>

<div>
    <h1>Pagina dos clientes do o PT <%= nomePt %> com ID <%= idPT %></h1>
    <hr>
    <%
    if(listaClientesFinal.size() == 0){
    	%><h2>Este PT não tem quaisquer clientes.</h2><%
    }
    else{
       	%>

		<div class="container">
		    <h2>Clientes:</h2>
		    <%
		    for(String o: listaClientesFinal) {
		        %>
		        <div>
		            <h3><%= o %></h3>
		            <form action="process_recommendation.jsp" method="post">
		                <!-- Dropdown for equipment selection -->
		                <label for="equipamento_<%= o %>">Selecionar Equipamento:</label>
		                <select id="equipamento_<%= o %>" name="equipamento_<%= o %>">
		                    <%

		                    // Create options for each available equipment
		                    for (String equipment : equipamentosDisponiveis) {
		                        %>
		                        <option value="<%= equipment %>"><%= equipment %></option>
		                        <%
		                    }
		                    %>
		                </select><br>
		
		                <!-- Input for date selection -->
		                <label for="data_<%= o %>">Escolher Data:</label>
		                <input type="date" id="data_<%= o %>" name="data_<%= o %>"><br>
		
		                <!-- Recommendation button -->
		                <input type="submit" value="Fazer Recomendação">
		            </form>
		        </div>
		        <%
		    }
		    %>
		</div>
	    <%
    }
    %>
</div>


</body>
</html>