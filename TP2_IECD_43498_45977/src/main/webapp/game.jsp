<%@ page import="TP1.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html>
<html>
<head>
<title>4-in-a-Row Game</title>
<link rel="stylesheet" type="text/css" href="css/game_css.css">
 <style>
        <%-- Add CSS styles here --%>
        <% User user2 = (User) session.getAttribute("user"); 
        String userColor = user2.getColor();%>
        body {
            background-color: <%= userColor != null ? userColor : "#ffffff" %>;
        }
    </style>
</head>
<body>
<%
    char[][] board = (char[][]) request.getAttribute("board");
    if (board == null) {
        // If the board attribute is not set, make a request to the servlet to initialize it
        request.getRequestDispatcher("FourInARowServlet").include(request, response);
        board = (char[][]) request.getAttribute("board");
    }
    int turn = (int) session.getAttribute("turn");
%>
<h1>4-in-a-Row Game</h1>
<table class="game-board">
    <% for (char[] row : board) { %>
        <tr>
            <% for (char cell : row) { %>
                <td><%= cell %></td>
            <% } %>
        </tr>
    <% } %>
</table>


	<p>Player's Turn: <%= (turn == 1) ? 'X' : 'O' %></p>
	<form method="POST" action="FourInARowServlet">
		<p>
			Choose a column (0-6): <input type="number" name="column" min="0"
				max="7">
		</p>
		<input type="submit" value="Make Move">
	</form>
	
	    <div class="message">
        <% String winner = (String) session.getAttribute("winner");
           if (winner != null) { %>
               Player <%= winner %> wins!
        <% } %>
    </div>
</body>
</html>
