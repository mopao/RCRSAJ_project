package database;

import java.sql.Connection;

public interface IDBConnection {
	
	/**
	 * retourne la connexion à la bd 
	 * @return 
	 */
	public Connection getDBconnection();

}
