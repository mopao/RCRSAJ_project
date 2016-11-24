package database;

public enum ConnectionType {
	
	SQLITE("SQLITE"),
	MYSQL("MYSQL");
	
	private final String type;       

	private ConnectionType(String t) {
	    type = t;
	}
	public String toString() {
	    return type;
	 }

}
