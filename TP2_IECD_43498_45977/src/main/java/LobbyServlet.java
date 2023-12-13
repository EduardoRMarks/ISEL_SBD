import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import TP1.User;
import TP1.UserXmlWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LobbyServlet")
public class LobbyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	User user;
	UserXmlWriter userXmlWriter;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Socket socket = (Socket) session.getAttribute("socket");
		BufferedReader in = (BufferedReader) session.getAttribute("in");
		PrintWriter out = (PrintWriter) session.getAttribute("out");

		user = (User) session.getAttribute("user");

		//System.out.println(user);

		request.setAttribute("nickname", user.getNickname());
		request.setAttribute("nationality", user.getNationality());
		request.setAttribute("age", user.getAge());
		request.setAttribute("picture", user.getProfilePicturePath());

		// Forward the request to the lobby.jsp
		request.getRequestDispatcher("lobby.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Socket socket = (Socket) session.getAttribute("socket");
		BufferedReader in = (BufferedReader) session.getAttribute("in");
		PrintWriter out = (PrintWriter) session.getAttribute("out");

		// Retrieve updated user information from the form
		String nickname = request.getParameter("nickname");
		if(nickname == null || nickname == "") {
			nickname = user.getNickname();
		}
		String password = request.getParameter("password");
		if(password == null || password == "") {
			password = user.getPassword();
		}
		String nationality = request.getParameter("nationality");
		if(nationality == "") {
			nationality = user.getNationality();
		}
		String age = request.getParameter("age");
		if(age == null || age == "") {
			age = user.getAge();
		}
		String picture = request.getParameter("picture");
		if(picture == null || picture == "") {
			picture = user.getProfilePicturePath();
		}
		String color = request.getParameter("color");
		if(color == null || color == "") {
			color = user.getColor();
		}

		userXmlWriter = (UserXmlWriter) session.getAttribute("userXmlWriter");

		User newUser = new User(nickname, password, nationality, age, picture, user.getWins(), user.getLosses(),
				user.getTime(), color);
		String novoXML = null;
		try {
			novoXML = userXmlWriter.updateWebInfo(user, newUser);
		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		session.setAttribute("user", newUser);
		System.out.println();
		System.out.println("LOBBY SERVLET NOVO XML");
		System.out.println();
		System.out.println(novoXML);
		out.println("Muda de picture");
		userXmlWriter.sendFullXML(out, novoXML);

		// Update user information in the data storage solution
		// Replace with actual update logic

		// Redirect to the GET request to display the updated information
		response.sendRedirect("LobbyServlet");
	}
}
