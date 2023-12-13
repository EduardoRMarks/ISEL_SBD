import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.catalina.util.XMLWriter;
import org.xml.sax.SAXException;

import TP1.User;
import TP1.UserXmlWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/StartMenuServlet")
public class StartMenuServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	String xmlDoc;
	UserXmlWriter userXmlWriter;
	User user;
    
    @Override
	public void init() throws ServletException {
		try {
			socket = new Socket("localhost", 9001); // Replace with the server IP address
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			xmlDoc = readFullXML(in);
			System.out.println(xmlDoc);
			userXmlWriter = new UserXmlWriter(xmlDoc);

			String input = in.readLine();
			System.out.println(input);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**@Override
	public void init() throws ServletException {

		xmlDoc = "<Users xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"Users.xsd\"> <user> <nickname losses=\"8\" time=\"121\" wins=\"10\">Roman</nickname> <password>bola</password> <nationality>Portugal</nationality> <age>24</age> <picture>/Users/roman/Desktop/Realm Grinder</picture> </user> <user> <nickname losses=\"10\" time=\"40\" wins=\"4\">Inacio</nickname> <password>qwerty</password> <nationality>Afghanistan</nationality> <age>15</age> <picture>/Users/roman/Desktop/d.pdf</picture> </user> <user> <nickname losses=\"0\" time=\"0\" wins=\"0\">qweq</nickname> <password>sad</password> <nationality>Andorra</nationality> <age>23</age> <picture/> </user> <user> <nickname losses=\"0\" time=\"20\" wins=\"2\">Andre</nickname> <password>123</password> <nationality>Armenia</nationality> <age>30</age> <picture>/Users/roman/Desktop/GoToGate_FB volta.html</picture> </user> <user> <nickname losses=\"0\" time=\"9\" wins=\"1\">LOL</nickname> <password>1212</password> <nationality>Uruguay</nationality> <age>22</age> <picture>/Users/roman/Desktop/GoToGate_FB volta.html</picture> </user> <user> <nickname losses=\"1\" time=\"39\" wins=\"1\">teste</nickname> <password>zxcv</password> <nationality>Israel</nationality> <age>50</age> <picture>/Users/roman/Desktop/Horário ISEl</picture> </user> </Users>";
		System.out.println(xmlDoc);
		try {
			userXmlWriter = new UserXmlWriter(xmlDoc);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}**/

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/start_menu.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	session.setAttribute("socket", socket);
    	session.setAttribute("in", in);
    	session.setAttribute("out", out);
    	session.setAttribute("userXmlWriter", userXmlWriter);
        String action = request.getParameter("action");
        if (action.equals("login")) {
            // Handle login action
           
            // Get the login form parameters
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            //System.out.println("LOGIN ATTEMPT for: " + username + " " + password);
            
            try {
				user = userXmlWriter.loginChecker(username, password);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            //System.out.println(user);
            
            if(user != null) {
            	session.setAttribute("user", user);
            	response.sendRedirect("LobbyServlet");
            }else {
            	request.setAttribute("loginError", true);
            	RequestDispatcher dispatcher = request.getRequestDispatcher("start_menu.jsp");
            	dispatcher.forward(request, response);
            	//request.getRequestDispatcher("/start_menu.jsp").forward(request, response);
            }
            
            //session.setAttribute("nickname", username);
            //session.setAttribute("password", password);
            
            // Redirect to the game servlet or another page
        } else if (action.equals("register")) {
            // Handle registration action
        	
            // Get the registration form parameters
        	 String nickname = request.getParameter("nickname");
             String password = request.getParameter("password");
             String nationality = request.getParameter("nationality");
             String age = request.getParameter("age");
             String imagePath = request.getParameter("imagePath");
             String color = request.getParameter("color");
            System.out.println("REGISTER ATTEMPT for: " + nickname + " " + password + " " + nationality 
            		+ " " + age + " " + imagePath);
            
            User user = new User(nickname, password, nationality, age, imagePath,"0","0","0", color);
            // Perform registration logic and validation
            // ...
            String newXML = null;
            session.setAttribute("user", user);
            try {
            	newXML = userXmlWriter.writeUserToFile(user);
			} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // Redirect to the registration success page or another page
            
			out.println("Novo registo");
			userXmlWriter.UpdateDoc(newXML);
			userXmlWriter.sendFullXML(out, newXML);
            response.sendRedirect("lobby.jsp");
        }
    }
    
    private static String readFullXML(BufferedReader in) throws IOException {
		String StringXML = "";
		int turn = 0;

		while (true) {
			String input = in.readLine();
			if (input.isBlank()) {
				input = in.readLine();
			}
			if (input.contains("</Users>")) {
				StringXML = StringXML + input;
				return StringXML;
			}
			if (turn != 0) {

				StringXML = StringXML + input;

			}
			turn = 1;
		}
	}	
}
