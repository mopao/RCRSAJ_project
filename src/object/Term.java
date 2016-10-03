package object;
/**
 * cette classe représente un attribut d'un atome
 * @author franck
 *
 */
public class Term {
	
	private String elt;
	private TermType type;
	private boolean isInHead=false;
	
	public boolean isInHead() {
		return isInHead;
	}

	public void setInHead(boolean isInHead) {
		this.isInHead = isInHead;
	}

	public Term(String elt, TermType type) {
		super();
		this.elt = elt;
		this.type = type;
	}

	public String getElt() {
		return elt;
	}

	public TermType getType() {
		return type;
	}
	
	public boolean isEquals(Term att){
		return elt.equals(att.elt) && type==att.type;
	}
	
	public String toString(){
		if(type==TermType.CONST)
			return "\""+elt+"\"";
		return elt;
		
	}

}
