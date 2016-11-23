package database;

/**
 * représente la connexion à une base de données mysql.
 */
import java.sql.Connection;

import java.sql.SQLException;


public class MYSQLConnection extends DBConnection {	
	
	private String address=null, userName=null, password=null;
	
    
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
	}


	@Override
	public Connection getDBconnection(String dbName)
			throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
