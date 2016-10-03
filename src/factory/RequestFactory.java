package factory;

import java.util.ArrayList;
import java.util.Iterator;

import object.Atom;
import object.ConjonctiveRequest;

public class RequestFactory {
	private ArrayList<String> RequestElements=null;

	public RequestFactory() {
		super();
		
	}
	
	public ConjonctiveRequest makeConjonctiveRequest(ArrayList<String> elts, String h) throws BadRequestException, SyntaxErrorException{
		
		this.RequestElements=elts;
		ArrayList<String> listRelationName= new ArrayList<String>();
		
		for (String rel : elts) {
			if(listRelationName.contains(rel.substring(0, rel.indexOf("(")))){				
				throw new BadRequestException("your request must be free self join.");
				
			}
			else{
				listRelationName.add(rel.substring(0, rel.indexOf("(")));				
			}
		}
		ArrayList<Atom> listAtoms= new ArrayList<Atom>();
		for (String rel : elts) {
			
			listAtoms.add(new Atom(rel,h));
			
		}
		
		return new ConjonctiveRequest(listAtoms,h);
		
	}

}
