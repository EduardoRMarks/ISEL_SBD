<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Date"%>
<%@page import="java.io.Console"%>
<%@page import="model.Pt" %>
<%@page import="model.Cliente" %>
<%@page import="model.UserRole" %>
<%@page import="util.DBConnectionManager"%>
<%@page import="util.PtUtil"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@ page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import= "java.util.ArrayList"%>
<%@page import= "java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Buscar Info Cliente</title>
</head>
	
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	List<String> listaClientes = new ArrayList<String>();
	String listaClientesJSON = null;
	StringBuilder jsArray = null;
	String paginaCliente = null;
	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT Nome FROM sbd_tp1_43498_45977_47739.cliente";
        
        listaClientes = PtUtil.getClientes(connection, query);
        
        jsArray = new StringBuilder();
        jsArray.append("[");
        for (String cliente : listaClientes) {
            jsArray.append("\"").append(cliente).append("\",");
        }
        if (!listaClientes.isEmpty()) {
            jsArray.setLength(jsArray.length() - 1);
        }
        jsArray.append("]");
        
	} 
	catch (Exception e) { e.printStackTrace(); } 
	finally { DBConnectionManager.close(null, statement, connection); }
%>

<div id="searchBar">
    <button id="voltarButton" onclick="redirectToPage('pt.jsp')">Voltar</button>
    <input type="text" id="searchInput" placeholder="Search for a client">
    <div id="searchResults" style="position: absolute; z-index: 1; background-color: white; display: none;"></div>
</div>

<script>
    const listaClientes = <%= jsArray.toString() %>;

    const searchInput = document.getElementById("searchInput");
    const searchResults = document.getElementById("searchResults");

    searchInput.addEventListener("input", function() {
        const inputText = this.value.trim().toLowerCase();
        if (inputText.length === 0) {
            searchResults.style.display = "none";
            return;
        }
        let matchingClients = listaClientes.filter(client => client.toLowerCase().startsWith(inputText));

        displaySearchResults(matchingClients);
    });


    function displaySearchResults(results) {
        searchResults.innerHTML = "";
        if (results.length > 0) {
            results.forEach(result => {
                const div = document.createElement("div");
                div.textContent = result;
                div.addEventListener("click", function(event) {
                    saveToSessionAndRedirect(result);
                });
                searchResults.appendChild(div);
            });
            searchResults.style.display = "block";
            searchInput.focus();
        } else {
            searchResults.style.display = "none";
        }
    }

    function saveToSessionAndRedirect(clientName) {
        try {
            const encodedClientName = encodeURIComponent(clientName);
            const url = 'clientPage.jsp?clientName=' + encodedClientName;
            window.location.assign(url);
        } catch (error) {
            console.error('Error:', error);
        }
    }

    document.addEventListener("click", function(event) {
        if (event.target !== searchInput && event.target !== searchResults) {
            searchResults.style.display = "none";
        }
    });

    searchInput.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            const inputText = this.value.trim().toLowerCase();
            if (listaClientes.includes(inputText)) {
            	const nomeCliente = encodeURIComponent(inputText);
            	request.getSession().setAttribute("nomeCliente", nomeCliente);
                redirectToPage('clientPage.jsp');
            } else {
                alert("Invalid client name. Please select from the suggestions or enter a valid name.");
            }
        }
    });

    function redirectToPage(escolha) {
        window.location.href = escolha;
    }
    
</script>

<%
    // ... your existing Java code
    
    // Handle setting the session attribute based on the parameters sent
    if ("true".equals(request.getParameter("saveToSession"))) {
        String clientName = request.getParameter("clientName");
        session.setAttribute("nomeCliente", clientName);
    }
%>



</body>
</html>