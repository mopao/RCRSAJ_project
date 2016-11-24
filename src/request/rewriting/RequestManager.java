package request.rewriting;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.IDBConnection;
import factory.BadRequestException;

import object.Atom;
import object.ConjonctiveRequest;
import object.RelationSchema;

public class RequestManager {
	
	private IConsistentRewriter rewriterSql;
	private IDBConnection conn =null;
	
	public RequestManager(IDBConnection conect) throws ClassNotFoundException, SQLException {
		super();
		rewriterSql=new SQLConsistentRewriter();
		conn=conect;
	}
	
	
	public ArrayList<ArrayList<String>> getRelationSchema(String table, String dbTorequest) throws ClassNotFoundException, SQLException{
		  Connection c=conn.getDBconnection(dbTorequest);
		  c.setAutoCommit(false);
	      DatabaseMetaData meta = c.getMetaData();
	      ResultSet res = meta.getColumns(null, null, table, null);
	      ArrayList<String> attributes=new ArrayList<String>();
	      ArrayList<String> typeAttributes=new ArrayList<String>();
	      ArrayList<ArrayList<String>> results=new ArrayList<ArrayList<String>>();
	      
	      while (res.next()) {
	         attributes.add(res.getString("COLUMN_NAME"));
	         typeAttributes.add(res.getString("TYPE_NAME"));
	      }
	      res.close();
	      c.close();
	      results.add(attributes);
	      results.add(typeAttributes);
	      return results;
	}
	/**
	 * associe à chaque atome de la requête q son schéma de 
	 * relation
	 * @param q : ConjonctiveRequest
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws BadRequestException 
	 */
	public ConjonctiveRequest setupRelationSchemas(ConjonctiveRequest q, String dbTorequest) throws ClassNotFoundException, SQLException, BadRequestException{
		for (Atom atom : q.getAtoms()) {
			ArrayList<ArrayList<String>> schema=getRelationSchema(atom.getName(), dbTorequest);
		      atom.setSchema(new RelationSchema(atom.getName(), schema.get(0), schema.get(1), atom.getKeySize()));
		      }
		
		return q;
		}
	
	/**
	 * associe à chaque atome de la requête q son schéma de 
	 * relation
	 * @param q : ConjonctiveRequest
	 * @param schemas : liste de schémas de relation
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws BadRequestException 
	 */
	public ConjonctiveRequest setupRelationSchemas(ConjonctiveRequest q, ArrayList<RelationSchema> schemas) throws BadRequestException{
		for (int i=0;i<q.getAtoms().size();i++) {
			q.getAtoms().get(i).setSchema(schemas.get(i));
		      }
		
		return q;
		}
	
	
	/**
	 * retourne ls réponses certaines à la requête non booléenne q
	 * @param q
	 * @return résultats de la requête q
	 * @throws SQLException 
	 * @throws BadRequestException 
	 * @throws ClassNotFoundException 
	 */
	public ArrayList<ArrayList<Object>> getNotboolCertainAnswer(ConjonctiveRequest q, String dbTorequest) throws ClassNotFoundException, BadRequestException, SQLException{
	    
		ArrayList<ArrayList<Object>> results=new ArrayList<>();
		ConjonctiveRequest q2=setupRelationSchemas(q, dbTorequest);
		
		SQLRequestFormat formattedRequest=new SQLRequestFormat(q2);
		ArrayList<Object> attributes=new ArrayList<>();
		for (SQLOperand op : formattedRequest.getH()) {
			attributes.add(op.getAttribute());
		}
		results.add(attributes);
		String request=rewriterSql.notBoolRequestRewriting(formattedRequest, new ArrayList<String>());
		Connection conn=this.conn.getDBconnection(dbTorequest);
		conn.setAutoCommit(false);
		Statement stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery( request+";" );
		while ( rs.next() ) {
			ArrayList<Object>result=new ArrayList<Object>();
	    	for (int i = 0; i < attributes.size(); i++) {
				result.add(rs.getObject(attributes.get(i).toString()));
				
			}	    	
	    	results.add(result);
	    	
	      }
	    rs.close();
	    stmt.close();
	    conn.close();
		
		return (results.size()>1)?results:null;
	}

	/**
	 * retourne la réponse certaine à la requête booléenne  q
	 * @param q
	 * @return résultat de la requête q
	 * @throws SQLException 
	 * @throws BadRequestException 
	 * @throws ClassNotFoundException 
	 */
	public String getboolCertainAnswer(ConjonctiveRequest q, String dbTorequest) throws ClassNotFoundException, BadRequestException, SQLException{
		String result="False";
		ConjonctiveRequest q2=setupRelationSchemas(q, dbTorequest);		
		SQLRequestFormat formattedRequest=new SQLRequestFormat(q2);
		String request=rewriterSql.boolRequestRewriting(formattedRequest, new ArrayList<String>());
		Connection conn=this.conn.getDBconnection(dbTorequest);
		conn.setAutoCommit(false);
		Statement stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery( request+";" );
		while ( rs.next() ) {
	    	result="True";
	    	break;	    	
	      }
		rs.close();
	    stmt.close();
	    conn.close();
		return result;
	}
	
	/**
	 * récupère les données dans une table
	 * @param table : nom de la table
	 * @return données de la table 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public ArrayList<ArrayList<Object>> selectTable(String table, String dbTorequest) throws ClassNotFoundException, SQLException{
		
		ArrayList<ArrayList<Object>> results=new ArrayList<ArrayList<Object>>();
		ArrayList<String>tableAttributes=getRelationSchema(table, dbTorequest).get(0);
		Connection conn=this.conn.getDBconnection(dbTorequest);
		conn.setAutoCommit(false);
		Statement stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM "+table+";" );		 
		int sizeTableAttributes=tableAttributes.size();
	    while ( rs.next() ) {
	    	ArrayList<Object>result=new ArrayList<Object>();
	    	for (int i = 0; i < sizeTableAttributes; i++) {
				result.add(rs.getObject(tableAttributes.get(i)));
			}
	    	results.add(result);
	    	
	      }
	    rs.close();
	    stmt.close();
	    conn.close();
		return results;
	}
	
	/**
	 * renvoie une connexion à la bd dbTorequest
	 * @return une connexion
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	/*private Connection getDbConnection(String dbTorequest) throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		
	    return DriverManager.getConnection("jdbc:sqlite:"+new File(dbTorequest+".db").getAbsolutePath());
	}*/
	
	/**
	 * renvoie la réécriture consistente de la requête q
	 * @param q: requête conjonctive
	 * @return q réécrit en sql
	 */
	public String consistentRewrote(ConjonctiveRequest q){
		SQLRequestFormat formattedRequest=new SQLRequestFormat(q);		
		ArrayList<String> list=new ArrayList<String>();
		String request="";
		if(q.getHead().size()==0)
			request=rewriterSql.boolRequestRewriting(formattedRequest, list);
		else
			request=rewriterSql.notBoolRequestRewriting(formattedRequest, list);
		
		return request;
		
	}
	

}
