package database;

import java.io.IOException;
import java.sql.SQLException;

/**
 * cette classe permet de charger une base de données à partir d'un fichier script.
 * @author franck
 *
 */
public interface IDBLoader {
	
	public void runScript( String scriptFile)throws IOException, SQLException;	

}
