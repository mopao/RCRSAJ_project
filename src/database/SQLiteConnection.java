package database;
/**
 * représente la connexion à une base de données sqlite.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection extends DBConnection{
	
	
	public SQLiteConnection(ConnectionType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Connection getDBconnection(String dbName) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("org.sqlite.JDBC");
	    return DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
	}

	

}
