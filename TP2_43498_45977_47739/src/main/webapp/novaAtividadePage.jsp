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
<title>Nova atividade</title>
</head>
<body>

	<%

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	
	String nifClube = null;
	
	List<String> atividadeIds = null;
   	List<String> nifsClubes = new ArrayList<>();
   	List<String> nomesClubes = new ArrayList<>();
	
	String idPt = (String) session.getAttribute("idPT");
	String atividadeId = null;
	//System.out.println("IDPT HAHHAA: " + idPt);

  	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.clube_pt WHERE IdPt = '" + idPt + "'";

        resultSet = PtUtil.getPtInfo(connection, query);
        
        while (resultSet.next()) {
        
        	nifClube = String.valueOf(resultSet.getInt("NifClube"));
        	nifsClubes.add(nifClube);
        }
        
        for (String s : nifsClubes)
        	System.out.println(s);
        
        query = "SELECT Id FROM sbd_tp1_43498_45977_47739.atividade";
        atividadeIds = PtUtil.getId(connection, query);

        Collections.sort(atividadeIds);

        // Get the highest value (last element after sorting) and increment it by 1
        int lastId = Integer.parseInt(atividadeIds.get(atividadeIds.size() - 1));
        atividadeId = String.valueOf(lastId + 1);

        System.out.println("ID ATIVIDADE: " + atividadeId);
        
        
        for (String s : nifsClubes) {
        	String queryClubes = "SELECT * FROM sbd_tp1_43498_45977_47739.clube WHERE Nif = '" + s + "'";
        	nomesClubes.add(PtUtil.getDesignacaoComercial(connection, queryClubes));
        }
        
        for (String s : nomesClubes)
            System.out.println(s);
	} 
  	
  	catch (Exception e) { e.printStackTrace(); } 
  	finally { DBConnectionManager.close(resultSet, statement, connection); }

%>

	<div>

		<h1>Publicar uma nova atividade</h1>
		<hr>
		<br>

		<form action="" method="post">

			<label for="nomeAtividade">Nome da Atividade:</label> <input
				type="text" id="nomeAtividade" name="nomeAtividade" required><br>
			<br> <label for="duracao">Duração:</label> <select id="duracao"
				name="duracao" required>
				<option value="15">15 Minutos</option>
				<option value="30">30 Minutos</option>
				<option value="45">45 Minutos</option>
				<option value="60">60 Minutos</option>
				<option value="75">75 Minutos</option>
				<option value="90">90 Minutos</option>
			</select> <br> <br> <label for="tipo">Tipo:</label> <select
				id="tipo" name="tipo" required onchange="handleTipoChange()">
				<option value="Semanal" selected>Semanal</option>
				<option value="Pontual">Pontual</option>
			</select> <br> <br> <label for="horaInicio">Hora de Início:</label>
			<input type="time" id="horaInicio" name="horaInicio" required>
			<br>

			<div id="semanalFields">
				<br>
				<!-- Fields specific to 'Semanal' type -->
				<label for="diaDeSemana">Dia da Semana:</label> <select
					id="diaDeSemana" name="diaDeSemana">
					<option value="Segunda-Feira">Segunda-Feira</option>
					<option value="Terça-Feira">Terça-Feira</option>
					<option value="Quarta-Feira">Quarta-Feira</option>
					<option value="Quinta-Feira">Quinta-Feira</option>
					<option value="Sexta-Feira">Sexta-Feira</option>
					<option value="Sábado">Sábado</option>
					<option value="Domingo">Domingo</option>
				</select>
			</div>

			<div id="pontualFields" style="display: none;">
				<br>
				<!-- Fields specific to 'Pontual' type -->
				<label for="data">Data:</label> <input type="date" id="data"
					name="data"><br>
			</div>
			<br> <label for="minParticipantes">Número Mínimo de
				Participantes:</label> <select id="minParticipantes" name="minParticipantes"
				required>
				<% for (int i = 1; i <= 10; i++) { %>
				<option value="<%= i %>"><%= i %></option>
				<% } %>
			</select><br> <br> <label for="maxParticipantes">Número
				Máximo de Participantes:</label> <select id="maxParticipantes"
				name="maxParticipantes" required>
				<% for (int j = 10; j <= 20; j++) { %>
				<option value="<%= j %>"><%= j %></option>
				<% } %>
			</select><br> <br> <label for="confirmacao">Confirmação:</label> <select
				id="confirmacao" name="confirmacao" required>
				<option value="0">Confirmar posteriormente</option>
				<option value="1">Confirmada</option>
			</select><br> <br> <label for="escalaoEtario">Escalão
				Etário:</label> <select id="escalaoEtario" name="escalaoEtario" required>
				<option value="18-30">18-30</option>
				<option value="31-45">31-45</option>
				<option value="46-69">46-69</option>
				<option value="70+">70+</option>
				<option value="todos">Todos</option>
			</select><br> 
			<br> 
			<label for="nifClube">Clube:</label>
			<select id="nifClube" name="nifClube" required>
			    <!-- Populate options for existing clubs -->
			    <% for (String clube : nomesClubes) { %>
			        <option value="<%= clube %>"><%= clube %></option>
			    <% } %>
			</select><br><br>
			
			<hr>
			<br>

			<button onclick="redirectToPagePublicarAtividade(event)">Publicar nova atividade</button>

		</form>
	</div>

	<script>
	
	function redirectToPagePublicarAtividade(event) {
		event.preventDefault();
	    console.log("estou aqui");

	    const nomeAtividade = document.getElementById('nomeAtividade').value.trim();
	    const horaInicio = document.getElementById('horaInicio').value.trim();

	    if (nomeAtividade !== '' && horaInicio !== '') {
	    	const atividadeId = '<%= atividadeId %>';
	    	const idPt = '<%= idPt %>';
	        const duracao = document.getElementById('duracao').value;
	        const tipo = document.getElementById('tipo').value;
	        const diaDeSemana = document.getElementById('diaDeSemana').value;
	        const data = document.getElementById('data').value;
	        const minParticipantes = document.getElementById('minParticipantes').value;
	        const maxParticipantes = document.getElementById('maxParticipantes').value;
	        const confirmacao = document.getElementById('confirmacao').value;
	        const escalaoEtario = document.getElementById('escalaoEtario').value;
	        const nifClube = document.getElementById('nifClube').value;
	        
	        var url = "publicarAtividadePage.jsp?atividadeId=" + encodeURIComponent(atividadeId) 
		        	+ "&idPt=" + encodeURIComponent(idPt) 
	        		+ "&nomeAtividade=" + encodeURIComponent(nomeAtividade) 
	        		+ "&estado=" + encodeURIComponent("InscricoesAbertas") 
	                + "&duracao=" + encodeURIComponent(duracao) 
	                + "&tipo=" + encodeURIComponent(tipo) 
	                + "&horaInicio=" + encodeURIComponent(horaInicio) 
	                + "&diaDeSemana=" + encodeURIComponent(diaDeSemana) 
	                + "&data=" + encodeURIComponent(data) 
	                + "&minParticipantes=" + encodeURIComponent(minParticipantes)
	                + "&maxParticipantes=" + encodeURIComponent(maxParticipantes)
	                + "&confirmacao=" + encodeURIComponent(confirmacao)
	                + "&escalaoEtario=" + encodeURIComponent(escalaoEtario)
	                + "&nifClube=" + encodeURIComponent(nifClube);
	
	    	console.log("Constructed URL:", url);
	        
	        
	        try {
	            console.log("Constructed URL:", url);
	            window.location.href = url;
	        } catch (error) {
	            console.error("Error redirecting:", error);
	        }
	    } else {
	        alert('Please fill in Name and / or Hour fields!');
	    }
	}

		function handleTipoChange() {
		    const tipo = document.getElementById('tipo').value;

		    // Hide both sets of fields initially
		    document.getElementById('semanalFields').style.display = 'none';
		    document.getElementById('pontualFields').style.display = 'none';

		    // Show the appropriate fields based on the selected 'Tipo'
		    if (tipo === 'Semanal') {
		        document.getElementById('semanalFields').style.display = 'block';
		    } else if (tipo === 'Pontual') {
		        document.getElementById('pontualFields').style.display = 'block';
		    }
		}
		
	</script>

</body>
</html>