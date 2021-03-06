package ORG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class Pool {
	private ArrayList<Connection> availableConnections = new ArrayList<Connection>();
	private int howManyAreOnline = 0, howManyAreOffline = 0, maxWaitingConnections;
	private String driver, url, user, password;
	
	public Pool(Properties props) {		
		this.driver = props.getProperty("driver");
		this.url = props.getProperty("url");
		this.user = props.getProperty("user");
		this.password = props.getProperty("password");
		this.howManyAreOffline = Integer.parseInt(props.getProperty("maxConnections"));
		this.maxWaitingConnections = Integer.parseInt(props.getProperty("maxWaitingCnx"));
		this.initializeConnectionPool();
	} 
	// Metodo para iniciar el pool de conexiones
	private void initializeConnectionPool() {
		while(!this.checkPoolIsAlmostFull()) { this.availableConnections.add(newConnection()); }
	}
	// Metodo para crear una conexion
	private Connection newConnection() {
		try {
			Class.forName(this.driver);			
			this.howManyAreOnline += 1;
			this.howManyAreOffline -= 1;
			return DriverManager.getConnection(this.url, this.user, this.password);
		} 
			
		catch(SQLException | ClassNotFoundException e) { e.printStackTrace(); }
		return null;
	}
	// Metodo para obtener una conexion del ArrayList
	public synchronized Connection getConnection() {
		Connection connection = null;
		do {
			if(this.availableConnections.size() > 0) { 
				connection = this.availableConnections.get(0);
				this.availableConnections.remove(0);
			}
			
			else { try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); } }
		} while(connection == null);
		
		this.checkMinConnections();
		return connection;
	}
	// Metodo para devolver una conexion al ArrayList
	public void returnConnection(Connection connection) {
		this.availableConnections.add(connection); 
		this.checkConnectionUnused();
	}	
	// Metodo para verificar si el pool se encuentra lleno
	private synchronized boolean checkPoolIsAlmostFull() {
		final int minSize = this.maxWaitingConnections;
		if(availableConnections.size() < minSize) { return false; }
		return true;
	}
	// Metodo para verificar si hay menos conexiones en espera de las recomendadas
	private synchronized void checkMinConnections() {
		if(this.availableConnections.size() < this.maxWaitingConnections) {
			while((this.availableConnections.size() < 5) && (this.howManyAreOffline > 0)){
				this.availableConnections.add(this.newConnection());
			}
		}
	}
	// Metodo para verificar si hay mas conexiones disponibles sin usar de la cantidad maxima de conexiones disponibles
	private synchronized void checkConnectionUnused() {
		if(this.availableConnections.size() > this.maxWaitingConnections) {
			while(this.availableConnections.size() > this.maxWaitingConnections) {
				try {
					this.availableConnections.get(0).close();
					this.availableConnections.remove(0);
					this.howManyAreOnline -= 1;
					this.howManyAreOffline += 1;
				} 
				
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
	}
	// Getters
	public int getHowManyAreOnline() { return this.howManyAreOnline; }
	public int getHowManyAreOffline() { return this.howManyAreOffline; }
	public int getActiveConnections() { return (this.howManyAreOnline - this.availableConnections.size()); }
	public int getInactiveConnections() { return this.availableConnections.size(); }
}