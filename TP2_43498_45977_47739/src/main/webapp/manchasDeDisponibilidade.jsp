<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.sql.Date"%>
<%@ page import="java.sql.Time"%>
<%@ page import="model.Cliente"%>
<%@ page import="model.UserRole"%>
<%@ page import="model.Pt"%>
<%@ page import="util.PtUtil"%>
<%@ page import="util.DBConnectionManager"%>
<%@ page import="util.ClientUtil"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Manchas de disponibilidade Page</title>
<style>
table {
	border-collapse: collapse;
	width: 100%;
}

th, td {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}

th {
	background-color: #f2f2f2;
}
</style>
</head>
<body>

	<%
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Cliente cliente = null;
    
    String idAtividade = null;
    String nomeAtividade = null;
    String estado = null;
    String duracao = null;
    String diaDeSemana = null;
    Time horaInicio = null;
    String tipo = null;
    Date data = null;
    String minParticipantes = null;
    String maxParticipantes = null;
    String confirmacao = null;
    String escalaoEtario = null;
    String nifClube = null;

   	List<List<String>> listaAtividadesConfirmadas = new ArrayList<>();
   	List<List<String>> listaAtividadesPorConfirmar = new ArrayList<>();
   	List<List<String>> listaAtividadesCanceladas = new ArrayList<>();
   	List<String> nomesParametrosTabela = new ArrayList<>();
   	
   	String idPT = (String) session.getAttribute("idPT");
   	String nomePt = (String) session.getAttribute("nomePt");
   	
    try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.atividade WHERE IdPt = '" + idPT + "'";

        resultSet = PtUtil.getPtInfo(connection, query);
        
        while (resultSet.next()) {
        
	        idAtividade = String.valueOf(resultSet.getInt("Id"));
	        nomeAtividade = resultSet.getString("Nome");
	        estado = resultSet.getString("Estado");
	        duracao = String.valueOf(resultSet.getInt("Duracao"));
	        diaDeSemana = resultSet.getString("DiaDeSemana");
	        horaInicio = resultSet.getTime("HoraDeInicio");
	        tipo = String.valueOf(resultSet.getString("Tipo"));
	        data = resultSet.getDate("Data");
	        minParticipantes = String.valueOf(resultSet.getInt("MinParticipantes"));
	        maxParticipantes = String.valueOf(resultSet.getInt("MaxParticipantes"));
	        confirmacao = resultSet.getString("Confirmacao");
	        escalaoEtario = resultSet.getString("EscalaoEtario");
	        nifClube = String.valueOf(resultSet.getInt("NifClube"));

            List<String> activityDetails = new ArrayList<>();
            activityDetails.add(idAtividade);
            activityDetails.add(nomeAtividade);
            activityDetails.add(estado);
            activityDetails.add(duracao);
            activityDetails.add(diaDeSemana);
            activityDetails.add(String.valueOf(horaInicio));
            activityDetails.add(tipo);
            activityDetails.add(String.valueOf(data));
            activityDetails.add(minParticipantes);
            activityDetails.add(maxParticipantes);
            activityDetails.add(escalaoEtario);
            activityDetails.add(nifClube);
            
            if (confirmacao.equals("0") && !estado.equals("Cancelado")) {
                listaAtividadesPorConfirmar.add(activityDetails);
            } else if (confirmacao.equals("1") && !estado.equals("Cancelado")) {
                listaAtividadesConfirmadas.add(activityDetails);
            }
            if (estado.equals("Cancelado")) {
            	listaAtividadesCanceladas.add(activityDetails);
            }
        }
                
        nomesParametrosTabela.add("IdAtividade");
        nomesParametrosTabela.add("NomeAtividade");
        nomesParametrosTabela.add("Estado");
        nomesParametrosTabela.add("Duracao");
        nomesParametrosTabela.add("DiaDeSemana");
        nomesParametrosTabela.add("HoraInicio");
        nomesParametrosTabela.add("Tipo");
        nomesParametrosTabela.add("Data");
        nomesParametrosTabela.add("MinParticipantes");
        nomesParametrosTabela.add("MaxParticipantes");
        nomesParametrosTabela.add("EscalaoEtario");
        nomesParametrosTabela.add("NifClube");

	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(resultSet, statement, connection);
    }

%>

	<div>

		<h1>
			Mancha de disponibilidade do PT
			<%= nomePt %>
			com ID
			<%= idPT %></h1>
		<hr>

		<div>
			<h2>Publicar uma nova atividade</h2>

			<button onclick="redirectToPage('novaAtividadePage.jsp')">Publicar
				Atividade</button>

		</div>

		<br>
		<hr>


		<div style="display: flex;">
			<div style="margin-right: 20px;">
				<h2>Atividades por confirmar</h2>
				<% if (listaAtividadesPorConfirmar.isEmpty()) { %>
				<h2>Não existem atividades por confirmar</h2>
				<% } else { %>
				<table style="width: auto; table-layout: fixed;">
					<thead>
						<tr>
							<% for (String nomeParametro : nomesParametrosTabela) { %>
							<th><%= nomeParametro %></th>
							<% } %>
							<th>Confirmar Atividade</th>
							<!-- Add a header for the buttons -->
						</tr>
					</thead>
					<tbody>
						<% for (List<String> activity : listaAtividadesPorConfirmar) { %>
						<tr>
							<% for (String detail : activity) { %>
							<td style="word-wrap: break-word;"><%= detail %></td>
							<% } %>
							<td>
								<button onclick="gerirAtividade('confirmar', '<%= activity.get(0) %>')">Confirmar atividade</button> <!-- Button per row -->
							</td>
						</tr>
						<% } %>
					</tbody>
				</table>
				<% } %>
			</div>
		</div>

		<br>
		<hr>

		<div style="display: flex;">
			<div style="margin-right: 20px;">
				<h2>Atividades confirmadas</h2>
				<% if (listaAtividadesPorConfirmar.isEmpty()) { %>
				<h2>Não existem atividades por confirmar</h2>
				<% } else { %>
				<table style="width: auto; table-layout: fixed;">
					<thead>
						<tr>
							<% for (String nomeParametro : nomesParametrosTabela) { %>
							<th><%= nomeParametro %></th>
							<% } %>
							<th>Cancelar Atividade</th>
						</tr>
					</thead>
					<tbody>
						<% for (List<String> activity : listaAtividadesConfirmadas) { %>
						<tr>
							<% for (String detail : activity) { %>
							<td style="word-wrap: break-word;"><%= detail %></td>
							<% } %>
							<td>
								<button onclick="gerirAtividade('cancelar', '<%= activity.get(0) %>')">Cancelar atividade</button>
							</td>
						</tr>
						<% } %>
					</tbody>
				</table>
				<% } %>
			</div>
		</div>

		<br>
		<hr>

		<div style="display: flex;">
			<div style="margin-right: 20px;">
				<h2>Atividades canceladas</h2>
				<% if (listaAtividadesCanceladas.isEmpty()) { %>
				<h2>Não existem atividades canceladas</h2>
				<% } else { %>
				<table style="width: auto; table-layout: fixed;">
					<thead>
						<tr>
							<% for (String nomeParametro : nomesParametrosTabela) { %>
							<th><%= nomeParametro %></th>
							<% } %>
						</tr>
					</thead>
					<tbody>
						<% 
                        for (List<String> activity : listaAtividadesCanceladas) { 
                            if (activity.get(nomesParametrosTabela.indexOf("Estado")).equals("Cancelado")) { %>
						<tr>
							<% for (String detail : activity) { %>
							<td style="word-wrap: break-word;"><%= detail %></td>
							<% } %>
						</tr>
						<% }
                        }
                    %>
					</tbody>
				</table>
				<% } %>
			</div>
		</div>



		<!-- JavaScript section -->
		<script>
		
	        function gerirAtividade(action, activityId) {
	        	
	        	console.log("action" + action);
	        	
	    	    var url = "gerirAtividadePage.jsp?action=" + encodeURIComponent(action) +
		        "&activityId=" + encodeURIComponent(activityId);

		    	console.log("Constructed URL:", url);
		    	
		    	window.location.href = url; // Redirect to the constructed URL

	        }
	        
	        function redirectToPage(escolha) {
	            window.location.href = escolha;
	        }

    	</script>

		<br>
		<hr>
		<br>

		<div>
			<button id="voltarButton" onclick="redirectToPage('pt.jsp')">Voltar</button>
		</div>
	</div>


</body>
</html>