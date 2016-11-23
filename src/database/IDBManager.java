package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.Observer;

public interface IDBManager{
	
	
	/**
	 * vérifie si au moins une bd a été sauvegardée
	 * @return retourne true si oui
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean isthereData() throws ClassNotFoundException, SQLException;
	
	/**
	 * 
	 * @return le nombre de bases de données enregistrées
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int getNumberofBd() throws ClassNotFoundException, SQLException;
	/**
	 * charge une base de données à partir d'un fichier script et l'enregistre
	 * @param scriptFile : nom du fichier script
	 * @param dbName : nom de la db a enregistrer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void loadDb(String scriptFile, String dbName) throws ClassNotFoundException, 
	IOException, SQLException;
	/**
	 * supprime une liste de bases de données
	 * @param dbNames : liste de noms de bases de données
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void removeDb(ArrayList<String> dbNames) throws ClassNotFoundException, SQLException;
	/**
	 * 
	 * @param dbName
	 * @return renvoie la liste des noms des tables de la bd  dbName.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ArrayList<String> getTableNames(String dbName) throws ClassNotFoundException, SQLException;

}
