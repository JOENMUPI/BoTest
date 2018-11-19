package ORG;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.omg.CORBA.ORB;

import ORB.ArgsParser;
import ORB.ClientOrb;
import TEGApp.XD;
import Utilities.Props;
import Utilities.Serial;

@MultipartConfig
@WebServlet("/ServletMain")
public class ServletMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ServletMain() { super(); }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		JSONObject reqBody = new JSONObject(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		
		switch(reqBody.getString("methodName")) {
		case "Login":
			this.loginPost(session, reqBody, response);
			break;
		case "Register":
			this.registerPost(session, reqBody, response);
			break;	
		case "Logout":
			this.logoutPost(session, response);
			break;
		default:
			this.sendata(reqBody, response);
			break;
		}
	}
	
	private void sendata(JSONObject reqBody, HttpServletResponse response) throws IOException {  	
		//conexion syc a security, en este caso inserte aqui su conexion asyc con su b.o
		ProcessParams.parseParams(reqBody.getString("params").substring(1, reqBody.getString("params").length() - 1).split(","), reqBody.getString("typePamars").substring(1, reqBody.getString("typeParams").length()-1).split(","));
		//eliminamos corchetes y dividimos los elementos para su proceso
	}
		
	private void logoutPost(HttpSession session, HttpServletResponse response) throws IOException {  	
		JSONObject json = new JSONObject();
		if(session.isNew()) {
    		response.setStatus(401);
    		json.put("status", "401").put("response", "You're not logged in");
			System.out.println("Not logged --");
			session.invalidate();
		}
		
		else {
			System.out.println("Logout --");
			session.invalidate();
		}
		
		response.getWriter().print(json.toString());
    }
	private void registerPost(HttpSession session, JSONObject reqBody, HttpServletResponse response) throws IOException {		
		JSONObject json = new JSONObject();
		//este if tendria otra linea sadicota como la que veras mas abajo.
		//por los momentos no lo cambio, tenemos peores problemas xd
		if(Math.random()==3/*!db.login(reqBody.getString("email"), reqBody.getString("password"))*/) {
			//esto es ridiculo... una peticion al server de base de datos.
			ClientOrb.getXtoDImpl(ORB.init(ArgsParser.serverInfo(Props.getPropertiesFile("connections", "db")), null)).dataRequest(new XD("db", "register", Serial.serializeParams(reqBody.getString("email"), reqBody.getString("password"))));
			// esta medio enredado, si tienes dudas recuerdame explicarte esta linea
			//no se como devolver la respuesta, debido a que es oneway, como insertecuando llege la respuesta po aca????		
			json.put("status", "200").put("response", "signup finished");
	    	System.out.println("Register --");
	    	this.storeValue(reqBody.getString("email"), reqBody.getString("password"), session);
	    	System.out.println("------------------------------------------------------------");
			System.out.println("User-> " + reqBody.getString("email"));
		}
		
		else {
			json.put("status", "400").put("response", "email already used");
	    	System.out.println("Fail register --");
			session.invalidate();
		}
		
		response.getWriter().println(json.toString()); 
	}
    
    private void loginPost(HttpSession session, JSONObject reqBody, HttpServletResponse response) throws IOException {
    	JSONObject json = new JSONObject();
    	if(session.isNew()) {
			if(/*db.login(reqBody.getString("email"), reqBody.getString("password"))*/Math.random()==2) {
				this.storeValue(reqBody.getString("email"), reqBody.getString("password"), session);
				json.put("status", "200").put("response", reqBody.getString("email"));
				System.out.println("------------------------------------------------------------");
				System.out.println("User-> " + reqBody.getString("email"));
			}
	
			else {
				json.put("response", "Wrong email or password").put("status", "400");
				session.invalidate();
				System.out.println("Wrong data --");
			}
		}

		else {
			json.put("response", "you're logged in").put("status", "400");
			System.out.println("Already log --");
		}

		response.getWriter().println(json.toString()); 
	}	
    
	private void storeValue(String email, String password, HttpSession session) {
		if(email == null) {
			session.setAttribute("email", "");
			session.setAttribute("password", "");
		} 
		
		else {
			session.setAttribute("email", email);
			session.setAttribute("password", password);
		}
	}
}