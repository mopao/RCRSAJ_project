package database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBConnection {
	
	
	/**
	 * retourne la connexion à la bd 
	 * @return 
	 */
	public Connection getDBconnection(String dbName)throws ClassNotFoundException, SQLException;
	
	public ConnectionType getType();

}
