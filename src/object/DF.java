package object;

import java.util.ArrayList;

import factory.BadRequestException;

public class DF  {
	
	private String key;
	private String dependency;
	
	public DF(Atom a) throws BadRequestException{
		ArrayList<String>keys=a.keys();
		key="";
		for (int i = 0; i < keys.size(); i++) {
			key+=keys.get(i);
		}
		
		dependency="";
		for (int i = a.getKeySize(); i < a.getAttributes().size(); i++) {
			
			if(a.getAttributes().get(i).getType()==TermType.VAR && !a.getAttributes().get(i).isInHead())
				dependency+=a.getAttributes().get(i).toString();
			
		}
		
	}

	public String getKey() {
		return key;
	}

	public String getDependency() {
		return dependency;
	}
    public String toString(){
    	return key+" -> "+dependency;
    }
	public boolean isEquals(DF d){
		
		return (key.equals(d.key) && dependency.equals(d.dependency))? true :  false;
	}
	
	

}
