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
   	
	List<String> listaNifClientesFinal = new ArrayList<String>();
   	List<String> listaClientesFinal = new ArrayList<String>();
   	List<String> equipamentosIdDisponiveisFinal = new ArrayList<String>();
   	List<String> equipamentosDisponiveisFinal = new ArrayList<String>();
   	
   	
   	String idPT = (String) session.getAttribute("idPT");
   	String nomePt = (String) session.getAttribute("nomePt");
   	
	System.out.println("IDPT: " + idPT);
	System.out.println("nome pt: " + nomePt);

    try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.pt_cliente_equipamento WHERE IdPt = '" + idPT + "'";

        resultSet = PtUtil.getPtInfo(connection, query);
        listaNifClientes = PtUtil.getNifClientes(connection, query);
        
        Set<String> clientesNifWithoutDuplicates = new LinkedHashSet<>(listaNifClientes);
        listaNifClientesFinal = new ArrayList<>(clientesNifWithoutDuplicates);
        
        nifCliente = String.valueOf(resultSet.getInt("NifCliente"));
        idEquipamento = resultSet.getString("IdEquipamento");
        nifClube = resultSet.getString("NifClube");
        data = resultSet.getDate("Data");
        uso = resultSet.getString("Uso");
        
        for (String s : listaNifClientes) {
        	String queryClientes = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Nif = '" + s + "'";
        	listaClientes.add(PtUtil.getNome(connection, queryClientes));
        }

        Set<String> setWithoutDuplicates = new LinkedHashSet<>(listaClientes);
        listaClientesFinal = new ArrayList<>(setWithoutDuplicates);

        // Fetch available equipment from the database
        query = "SELECT Id FROM sbd_tp1_43498_45977_47739.equipamento WHERE Estado = 1";
        equipamentosDisponiveis = PtUtil.getEquipamentosDisponiveis(connection, query);
        
        Set<String> equipamentosIdWithoutDuplicates = new LinkedHashSet<>(equipamentosDisponiveis);
        equipamentosIdDisponiveisFinal = new ArrayList<>(equipamentosIdWithoutDuplicates);
        
        for (String s : equipamentosIdDisponiveisFinal) {
        	String queryEquipamento = "SELECT Nome FROM sbd_tp1_43498_45977_47739.equipamento WHERE Id = '" + s + "'";
        	equipamentosDisponiveisFinal.add(PtUtil.getNomeEquipamento(connection, queryEquipamento));
        }

        Set<String> equipamentosWithoutDuplicates = new LinkedHashSet<>(equipamentosDisponiveisFinal);
        equipamentosDisponiveisFinal = new ArrayList<>(equipamentosWithoutDuplicates);
        
        for(String s : equipamentosDisponiveisFinal) {
        	System.out.println(s);
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
		    for(int i = 0; i < listaNifClientesFinal.size(); i++) {
		    	int equipIndex = 0;
		        %>
		        <div>
			        <h3><%= listaClientesFinal.get(i) %> - NIF: <%= listaNifClientesFinal.get(i) %></h3>
			        <form action="" method="post">
			
			            <!-- Dropdown for equipment selection -->
			            <label for="equipamento_<%= i %>">Selecionar Equipamento:</label>
			            <select id="equipamento_<%= i %>" name="equipamento_<%= i %>">
							<%
							for (int j = 0; j < equipamentosIdDisponiveisFinal.size(); j++) {
							    String equipmentId = equipamentosIdDisponiveisFinal.get(j);
							    String equipmentName = equipamentosDisponiveisFinal.get(j);
							    String selected = "";
							    if (equipmentId.equals(idEquipamento)) {
							        selected = "selected";
							    }
							%>
							<option value="<%= equipmentId %>" <%= selected %>><%= equipmentName %></option>
							<%
							}
							%>
			            </select><br>
			
			            <!-- Input for date selection -->
			            <label for="data_<%= i %>">Escolher Data:</label>
			            <input type="date" id="data_<%= i %>" name="data_<%= i %>"><br>
			
						<script>
						    function convertDateToString(index) {
						        var selectedDate = document.getElementById("data_" + index).value;
						        // You can use 'selectedDate' as needed, such as sending it to the server or performing further actions
						    }
						</script>
			
		                <!-- Dropdown for binary choice -->
		                <label for="choice_<%= i %>">Uso:</label>
		                <select id="choice_<%= i %>" name="choice_<%= i %>">
		                    <option value="1">Sim</option>
		                    <option value="0">Não</option>
		                </select><br>
			
						<script>
						    function getDropdownValue(index) {
						        var dropdown = document.getElementById("choice_" + index);
						        var selectedValue = dropdown.value;
						    }
						</script>

						<br>
			
						               <!-- Button to trigger the redirection -->
                <div class="container">
                
                    <!-- Pass necessary Java variables to JavaScript function -->
					<button type="button" onclick="redirectToPageRecomendacao(
					    '<%= idPT %>',
					    '<%= listaNifClientesFinal.get(i) %>',
						'<%= equipamentosIdDisponiveisFinal.get(equipIndex) %>',
						'<%= i %>'
					)">Fazer Recomendacao</button>
                </div>
            </form>
        </div>
        <%
    }
    %>
</div>
<%
}
%>

<!-- JavaScript section -->
<script>
	function redirectToPageRecomendacao(idPT, nifCliente, index) {
		
	    var selectedDate = document.getElementById("data_" + index).value;
	    var dropdownValue = document.getElementById("choice_" + index).value;
	    var equipmentDropdown = document.getElementById("equipamento_" + index);
	    var equipmentId = equipmentDropdown.options[equipmentDropdown.selectedIndex].value;
	
	    var url = "recomendacoesPage.jsp?idPt=" + encodeURIComponent(idPT) +
	        "&nifCliente=" + encodeURIComponent(nifCliente) +
	        "&idEquipamento=" + encodeURIComponent(equipmentId) +
	        "&nifClube=505200597" +
	        "&data=" + encodeURIComponent(selectedDate) +
	        "&uso=" + encodeURIComponent(dropdownValue) +
	        "&action=fazerRecomendacao";
	
	    console.log("Constructed URL:", url);
	
	    window.location.href = url; // Redirect to the constructed URL
	}
</script>

<br>
<hr>
<br>

<div>
    <button id="voltarButton" onclick="redirectToPage('pt.jsp')">Voltar</button>
</div>
<script>
    function redirectToPage(escolha) {
        window.location.href = escolha; }

</script>
</body>
</html>