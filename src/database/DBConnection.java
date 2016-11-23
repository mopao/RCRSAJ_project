package database;

public abstract class DBConnection implements IDBConnection{
	
private ConnectionType type=null;
	
	public DBConnection(ConnectionType type){
		this.type=type;
	}
	
	@Override
	public ConnectionType getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
