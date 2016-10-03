package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * cette classe permet de charger une base de données sql à partir d'un fichier script.
 * @author franck
 *
 */
public class SQLDBLoader implements IDBLoader{
	
	private static final String DEFAULT_DELIMITER = ";";
    
    public static final Pattern delimP = Pattern.compile("^\\s*(--)?\\s*delimiter\\s*=?\\s*([^\\s]+)+\\s*.*$", Pattern.CASE_INSENSITIVE);

	private Connection conn;
	private String delimiter= DEFAULT_DELIMITER;
	private boolean autoCommit, isFullrequest=false;
	
	public SQLDBLoader(Connection conn, boolean autoCommit) {
		super();
		this.conn = conn;
		this.autoCommit = autoCommit;
		
	}
	
	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter;        
        isFullrequest = fullLineDelimiter;
    }
	
	 /**
	  * execute un sript sql
	  * @param scriptFile le chemin vers le fichier script 
	  * @throws IOException
	  * @throws SQLException
	  */
    public void runScript(String scriptFile) throws IOException, SQLException {
    	Reader reader;
    	reader=new BufferedReader(new FileReader(scriptFile));
        try {
            boolean originalAutoCommit = conn.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    conn.setAutoCommit(this.autoCommit);
                }
                runScript(reader);
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        } catch (IOException | SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }
    
    /**
     *execute un sript sql
	 * @param reader source du script
     * @throws IOException
     * @throws SQLException
     */
    private void runScript(Reader reader) throws IOException,
    SQLException {
	StringBuffer command = null;
	try {
	    LineNumberReader lineReader = new LineNumberReader(reader);
	    String line;
	    while ((line = lineReader.readLine()) != null) {
	        if (command == null) {
	            command = new StringBuffer();
	            isFullrequest=false;
	        }
	        String trimmedLine = line.trim();
	        final Matcher delimMatch = delimP.matcher(trimmedLine);
	        if (trimmedLine.length() < 1
                    || trimmedLine.startsWith("//")||trimmedLine.startsWith("/*")|| trimmedLine.startsWith("SET")) {
                // Do nothing
            } 
	        else if (delimMatch.matches()) {
	            setDelimiter(delimMatch.group(2), false);
	        } 
	        else if (trimmedLine.startsWith("--")) {
                //println(trimmedLine);
            } else if (trimmedLine.length() < 1
                    || trimmedLine.startsWith("--")) {
                // Do nothing
            }
	        else if (trimmedLine.endsWith(getDelimiter())||
	                trimmedLine.equals(getDelimiter())) {
	            command.append(line.substring(0, line
	                    .lastIndexOf(getDelimiter())));
	            command.append(" ");
	            isFullrequest=true;
	           
	        } else {
	        	if(trimmedLine.endsWith(",")){
	        		if(line.contains("COMMENT")){
		        		line=line.substring(0, line.indexOf("COMMENT"))+",";
		        	}
		            if(line.contains("DEFAULT")){
		        		line=line.substring(0, line.indexOf("DEFAULT"))+",";
		        	}
	        		
	        	}
	        	else{
	        		if(line.contains("COMMENT")){
		        		line=line.substring(0, line.indexOf("COMMENT"));
		        	}
		            if(line.contains("DEFAULT")){
		        		line=line.substring(0, line.indexOf("DEFAULT"));
		        	}
	        	}
	        	
	            command.append(line);
	            command.append("\n");
	        }
	        
	        if(isFullrequest){
	        	if(command.toString().startsWith("CREATE") || 
	        			command.toString().startsWith("INSERT") ){
	        		executeRequest( command.toString().substring(0, 
	        				command.toString().lastIndexOf(")")+1), lineReader);
	        		
	        	}
	        	command = null;
		        	   
	        }
	    }
	    /*if (command != null) {
	    	this.execCommand(conn, command, lineReader);
	    }*/
	    if (!autoCommit) {
	        conn.commit();
	    }
	    
	} catch (Exception e) {
	    throw new IOException(String.format("Error executing '%s': %s", command, e.getMessage()), e);
	} finally {
	    conn.rollback();
	}
}
	/**
	 * execute une requête sql
	 * @param request requête sql
	 * @throws SQLException
	 */
    private void executeRequest( String request, LineNumberReader lineReader) throws SQLException {
//    	System.out.println( request);
		Statement statement = conn.createStatement();
		try {
		   statement.execute(request);
		} catch (SQLException e) {	
			
		        throw new SQLException( e.getMessage()+ "line : "+lineReader.getLineNumber(), e);
		    
		}

		if (autoCommit && !conn.getAutoCommit()) {
		    conn.commit();
		}		

		try {
		    statement.close();
		} catch (Exception e) {
		    // Ignore to workaround a bug in Jakarta DBCP
		}
	}
    

    private String getDelimiter() {
        return delimiter;
    }	
    /**
     * ferme la connexion 
     * @throws SQLException
     */
   

}
