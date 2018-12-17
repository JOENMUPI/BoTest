package ORG;

import java.io.IOException;
import java.security.SecureRandom;
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
import TEGApp.CB;
import TEGApp.XD;
import Utilities.Props;
import Utilities.Serial;

@MultipartConfig
@WebServlet("/ServletMain")
public class ServletMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ServletMain() { super(); }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getParameter("methodName")) {
		case "Logout":
			this.logout(request, response);
			break;
		default:
			this.sendata(request, response);
			break;
		}
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		JSONObject reqBody = new JSONObject(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		
		switch(reqBody.getString("methodName")) {
		case "Login":
			this.login(session, reqBody, response);
			break;
		case "Register":
			this.register(session, reqBody, response);
			break;
		default:
			this.sendata(session, reqBody, response);
			break;
		}
	}
	
	private void sendata(HttpSession session, JSONObject reqBody, HttpServletResponse response) throws IOException {  	
		JSONObject json = new JSONObject();
		try { 
			if(!session.isNew()) {
				CB cb = new CB("" + new SecureRandom().nextLong(), reqBody.getString("methodName"), Serial.serializeParams(ProcessParams.parseParams(this.fragment(reqBody.getString("params")), this.fragment(reqBody.getString("typeParams")))));
				//falta crear las interfaces de los b.o, con los elemento ya constuidos aqui solo falta enviarlo
			} else { session.invalidate(); }
		} catch(Exception e) {
			e.printStackTrace();
			json.put("status", 500).put("response", "Internal error: " + e);
		} finally { response.getWriter().println(json); }
	}
	
	private void sendata(HttpServletRequest request, HttpServletResponse response) throws IOException {  	
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		try { 
			if(!session.isNew()) { 
				CB cb = new CB("" + new SecureRandom().nextLong(), request.getParameter("methodName"), Serial.serializeParams(ProcessParams.parseParams(this.fragment(request.getParameter("params")), this.fragment(request.getParameter("typePamars")))));
				//falta crear las interfaces de los b.o, con los elemento ya constuidos aqui solo falta enviarlo
			} else { session.invalidate(); }
		} catch(Exception e) {
			e.printStackTrace();
			json.put("status", 500).put("response", "Internal error: " + e);
		} finally { response.getWriter().println(json); }
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
    	JSONObject json = new JSONObject();
		if(session.isNew()) {
    		json.put("status", "401").put("response", "You're not logged in");
			session.invalidate();
		} else { session.invalidate(); }
		response.getWriter().print(json.toString());
	}
	
	private void register(HttpSession session, JSONObject reqBody, HttpServletResponse response) throws IOException {		
		JSONObject json = new JSONObject();
		try {
			if(Serial.deserializeDS(ClientOrb.getXtoDImpl(ORB.init(ArgsParser.serverInfo(Props.getPropertiesFile("connections", "server")), null)).dataRequest(new XD("user", "checkUser", Serial.serializeParams(new Object[] { reqBody.getString("user"), reqBody.getString("pass") }))).obj).hasNext()) {
				json.put("status", 400).put("response", "email already used");
		    	session.invalidate();
		    } else {
		    	ClientOrb.getXtoDImpl(ORB.init(ArgsParser.serverInfo(Props.getPropertiesFile("connections", "server")), null)).dataRequest(new XD("db", "login", Serial.serializeParams(new Object[] { reqBody.getString("user"), reqBody.getString("pass") })));
		    	json.put("status", 200).put("response", "signup finished");
		    	this.storeValue(reqBody.getString("email"), reqBody.getString("password"), session);
			}
		} catch(Exception e) { 
			e.printStackTrace();
			json.put("status", 500).put("response", "Internal error " + e);
		} finally { response.getWriter().println(json); } 
	}
    
    private void login(HttpSession session, JSONObject reqBody, HttpServletResponse response) throws IOException {
    	JSONObject json = new JSONObject();
    	try {
	    	if(session.isNew()) {
				if(Serial.deserializeDS(ClientOrb.getXtoDImpl(ORB.init(ArgsParser.serverInfo(Props.getPropertiesFile("connections", "db")), null)).dataRequest(new XD("user", "login", Serial.serializeParams(new Object[] { reqBody.getString("user"), reqBody.getString("pass") }))).obj).hasNext()) {
					this.storeValue(reqBody.getString("user"), reqBody.getString("pass"), session);
					json.put("status", 200).put("response", reqBody.getString("email"));
				} else {
					json.put("response", "Wrong email or password").put("status", 400);
					session.invalidate();
				}
	    	} else { json.put("response", "you're logged in").put("status", 400); }
    	} catch(Exception e) { 
    		json.put("status", 500).put("response", "Internal error: " + e); 
    		e.printStackTrace(); 
    	} finally { response.getWriter().println(json); }
	}	
    
    private String[] fragment(String array) {
    	return array.substring(1, array.length() - 1).split(",");
    }
    
	private void storeValue(String user, String pass, HttpSession session) {
		session.setAttribute("user", user);
		session.setAttribute("pass", pass);
	}
}