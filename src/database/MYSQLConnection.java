package database;

/**
 * représente la connexion à une base de données mysql.
 */
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;


public class MYSQLConnection extends DBConnection {	
	
	private String address=null, userName=null, password=null, dbName=null;
	
    
	public MYSQLConnection(ConnectionType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
    
	
	public MYSQLConnection(ConnectionType type, String address,
			String userName, String password) {
		super(type);
		this.address = address;
		this.userName = userName;
		this.password = password;
		//dbName=db;
	}


	@Override
	public Connection getDBconnection(String dbName)
			throws ClassNotFoundException, SQLException {
		
		// TODO Auto-generated method stub
		//STEP 1: Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");

	    //STEP 2: Open a connection
	    System.out.println("Connecting to database...");
	    return DriverManager.getConnection("jdbc:mysql://"+address+"/"+dbName,userName,password);
		
	}	

}
