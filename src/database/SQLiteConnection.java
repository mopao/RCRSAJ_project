package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection implements IDBConnection{
	
	private ConnectionType type=null;
	
	public SQLiteConnection(ConnectionType type){
		this.type=type;
	}

	@Override
	public Connection getDBconnection(String dbName) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("org.sqlite.JDBC");
	    return DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
	}

	@Override
	public ConnectionType getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
