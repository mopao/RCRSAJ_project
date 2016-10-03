package database;

import interfaces.Observable;
import interfaces.Observer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * cette classe s'ocupe de la gestion des bases de données enregistrées
 * @author franck
 *
 */
public class DBManager implements Observer {

	
	// nom de la base de données des noms de base de données
	private static final String dbName="dbData.db";
	
	//nom de la table des noms de base de données
	private static final String dbTableName="tableDataName";
	
	private ArrayList<Observable> observables=new ArrayList<>();
	
	public DBManager() throws ClassNotFoundException, SQLException{
		
		Connection c=getDbConnection();
		Statement stmt = c.createStatement();
	    String sql = "CREATE TABLE IF NOT EXISTS " +dbTableName+
	                   "(dbname TEXT PRIMARY KEY     NOT NULL)"; 
	    stmt.executeUpdate(sql);
	    stmt.close();
	    c.close();
	}
	
	/**
	 * 
	 * @return renvoie la connexion à la base de données des noms de base de données
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getDbConnection() throws ClassNotFoundException, SQLException{
		
			Class.forName("org.sqlite.JDBC");
		    return DriverManager.getConnection("jdbc:sqlite:"+dbName);	
	}
	
	/**
	 * vérifie si au moins une bd a été sauvegardée
	 * @return retourne true si oui
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean isthereData() throws ClassNotFoundException, SQLException{
		
		int count=getNumberofBd();
	    
	    if(count>0)
	    	return true;
		
		return false;
	}
	
	/**
	 * 
	 * @return le nombre de bases de données enregistrées
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int getNumberofBd() throws ClassNotFoundException, SQLException{
		String sql ="SELECT COUNT(*) AS NumberOfBd FROM "+dbTableName+";";
		Connection conn=getDbConnection();
	    conn.setAutoCommit(false);
		Statement stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql);
		rs.next(); 
		int count = rs.getInt("NumberOfBd");
	    rs.close();
	    stmt.close();
	    conn.close();
	    return count;
	}
	/**
	 * 
	 * @return renvoie la connexion à la base de données dbName
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getDbConnection(String dbName) throws ClassNotFoundException, SQLException{
		
			Class.forName("org.sqlite.JDBC");
		    return DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");	
	}
	
	/**
	 * enregistre le nom d'une base de données 
	 * @param dbName : nom de la base de données
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void recordDb(String dbName) throws ClassNotFoundException, SQLException{
		Connection c=getDbConnection();
		c.setAutoCommit(false);
		String sql = "INSERT INTO "+dbTableName+" (dbname) " +
                "VALUES (\'"+dbName+"\');"; 
		Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);        
        c.commit();
        stmt.close();
	    c.close();
	}
	/**
	 * supprime une base de données
	 * @param dbName 
	 */
	private void deleteDbFile(String dbName){
		File f = new File(dbName+".db");
		if(f.exists() && !f.isDirectory()) { 
		    f.delete();
		}
	}
	
	/**
	 * supprime le nom d'une base de données
	 * @param dbName 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	private void deleteDbName(String dbName) throws ClassNotFoundException, SQLException{		
		Connection c=getDbConnection();
		c.setAutoCommit(false);
		String sql = "DELETE FROM "+dbTableName+" WHERE dbname=\'"+dbName+"\';"; 
		Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);        
        c.commit();
        stmt.close();
	    c.close();
	    deleteDbFile(dbName);
	}
	/**
	 * charge une base de données à partir d'un fichier script et l'enregistre
	 * @param scriptFile : nom du fichier script
	 * @param dbName : nom de la db a enregistrer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void loadDb(String scriptFile, String dbName) throws ClassNotFoundException, IOException, SQLException{
		
		
	    Connection con=getDbConnection(dbName);	
		IDBLoader loader=new SQLDBLoader(con, false);
		try {
			loader.runScript(scriptFile);
			recordDb(dbName);
			updateObservables(getNumberofBd());
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			deleteDbFile(dbName);
			throw e;
		}
				
	}
	
	/**
	 * supprime une liste de bases de données
	 * @param dbNames : liste de noms de bases de données
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void removeDb(ArrayList<String> dbNames) throws ClassNotFoundException, SQLException{
		
		
        for (String dbName : dbNames) {
        	deleteDbName(dbName);
		}
        updateObservables(getNumberofBd());
	}
	
	public String [] getDbNames() throws ClassNotFoundException, SQLException{
		
		int count=getNumberofBd();
		String [] listDbNames=new String[count];
		Connection conn=getDbConnection();
		conn.setAutoCommit(false);
		Statement stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM "+dbTableName+";" );
		
		int i=0;
	    while ( rs.next() ) {
	    	String  name = rs.getString("dbname");
	    	listDbNames[i]=name;
	        i++;
	      }
	      rs.close();
	      stmt.close();
	      conn.close();
		return (listDbNames.length>0)? listDbNames:null;
	}
	
	public ArrayList<String> getTableNames(String dbName) throws ClassNotFoundException, SQLException{
		
		ArrayList<String> results=new ArrayList<>();
		Connection c=getDbConnection(dbName);
		DatabaseMetaData meta = c.getMetaData();
	    ResultSet res = meta.getTables(null, null,  "%", 
	         new String[] {"TABLE"});
	     
	      while (res.next()) {
	         results.add(res.getString("TABLE_NAME"));
	      }
	      res.close();
	      c.close();
	      return (results.size()>1)? results:null;
	}
	

	@Override
	public void addObservable(Observable ob) {
		// TODO Auto-generated method stub
		observables.add(ob);
	}
	
	public void updateObservables(int compl){
		for (Observable ob : observables) {
			ob.setView(compl);
			
		}
	}

}