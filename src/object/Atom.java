package object;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import factory.BadRequestException;
import factory.SyntaxErrorException;
/**
 * cette classe représente un atome d'une requête conjonctive
 * @author franck
 *
 */
public class Atom {
	
	private ArrayList<Term> attributes;
	private int keySize;
	private String name;
	private ArrayList<Term> keys;
	private RelationSchema schema;
	
	
	public ArrayList<Term> getAttributes() {
		return attributes;
	}
	public int getKeySize() {
		return keySize;
	}
	public String getName() {
		return name;
	}
	
	public RelationSchema getSchema() {
		return schema;
	}
	public void setSchema(RelationSchema schema) throws BadRequestException{
		if(schema.getRelationName().equals(name) && getSize()==schema.getSize())
			this.schema = schema;
		else{
			BadRequestException error=new BadRequestException("Can't find relation name \'"+ name+"\' " +
					"with size "+getSize()+" in database.");
			error.setErrorType("Problem with "+ name+"-atom !");
			throw error;
		}
			
	}
	
	public int getSize(){
		return attributes.size();
	}
	
	public Atom (String atom, String requestHead) throws BadRequestException, SyntaxErrorException{
		name=atom.substring(0, atom.indexOf("("));
		attributes=new ArrayList<Term>();
		int k = atom.indexOf("(")+1;
	    String VARIABLE_REGEX ="[a-z]";
	    Pattern p = Pattern.compile(VARIABLE_REGEX);
        
		while( k < atom.length()-1){
			if(atom.charAt(k)!=',' && atom.charAt(k)!=';' ){

				if(atom.charAt(k)=='\"'){
				
					int j= atom.substring(k+1).indexOf("\"");					
					attributes.add(new Term(atom.substring(k+1, k+j+1),TermType.CONST ));
					k+=j+2;
				}
				else{
					Matcher m = p.matcher(atom.substring(k, k+1));
					if( m.matches()){
						//if(atom.substring(k+1).indexOf(atom.substring(k, k+1))==-1){
							Term t=new Term(atom.substring(k, k+1),TermType.VAR );
							if(requestHead.isEmpty()||requestHead.indexOf(atom.substring(k, k+1))==-1)
								attributes.add(t);
							else{
								t.setInHead(true);
								attributes.add(t);
							}
								
							k++;
						/*}
						else
							throw new BadRequestException("The terms of each atom must be distinct.");*/
						
					}
					else
						throw new SyntaxErrorException("incorrect syntax in the "+name+"-atom.");
					
						
				}
			}
			else k++;
			
		}
		
		keySize=atom.substring(atom.indexOf("(")+1, atom.indexOf(";")).split(",").length;
		//System.out.println(keySize);
		keys=new ArrayList<Term>();
		for (int i = 0; i < keySize; i++) {
			if(attributes.get(i).getType()==TermType.VAR && !attributes.get(i).isInHead())
				keys.add(attributes.get(i));
			
		}
		
		/*if(keys.size()==0){
			throw new BadRequestErrorException("No key variable in one atom of query.");			
		}*/
		
	}
	
	public DF df() throws BadRequestException{
		return new DF(this);
	}
	
	public ArrayList<String> keys(){
		
		ArrayList<String> key=new ArrayList<String>(keys.size());
		for (int i = 0; i < keys.size(); i++) {			
				key.add(keys.get(i).getElt());
			
		}
		return key;
	}
	/**
	 * teste l'égalité avec une liste de termes
	 * @param atts: ArrayList<Term>
	 * @return true si égalité 
	 */
	private boolean attributEquals(ArrayList<Term> atts){
		if(atts.size()!=attributes.size())
			return false;
		else{
			for (int i = 0; i < atts.size(); i++) {
				if(!atts.get(i).isEquals(attributes.get(i)))
					return false;
			}
		}
		
		return true;
	}
	/**
	 * teste l'égalité avec une autre atome
	 * @param R : Atom
	 * @return true si égalité
	 */
	public boolean isEquals(Atom R){
		return keySize==R.keySize && attributEquals(R.attributes) && name.equals(R.name);
	}
	
	/**
	 * teste si l'atome courant est de clé égale avec un autre atome
	 * @param R : Atom
	 * @return true si égalité
	 */
	public boolean isKeyEquals(Atom R){
		if(R.keySize!=keySize)
			return false;
		else{
			for (int i = 0; i < keys.size(); i++) {
				
				if(!R.keys.get(i).isEquals(keys.get(i)))
					return false;
				
			}
		}
		
		return true;
	}
		
	
	public String toString(){
		
		String result=name+"(";
		for (int i = 0; i < attributes.size(); i++) {
			if(i==attributes.size()-1 && i==keySize-1)
				result+=attributes.get(i).toString()+";)";
			else if(i==attributes.size()-1)
				result+=attributes.get(i).toString()+")";
			else if(i==keySize-1)
				result+=attributes.get(i).toString()+";";
			else
				result+=attributes.get(i).toString()+",";
			
		}
		return result; 
		
	}
	public ArrayList<Term> getKeys() {
		return keys;
	}
	
	/**
	 * retourne les variables de l'atome
	 * @return String
	 */
	public String getVars(){
		String vars="";
		for (int i = 0; i < attributes.size(); i++) {
			if(attributes.get(i).getType()==TermType.VAR && !attributes.get(i).isInHead())
				vars+=attributes.get(i).toString();
			
		}
		
		return vars;
	}
	
	/**
	 * renvoie la position du terme t dans l'atome.
	 * on considère que le premier terme de l'atome occupe la position zéro
	 * @param t: Term
	 * @return int
	 */
	public int indexAt(Term t){
		
		int pos=-1;
		for (int i = 0; i < attributes.size(); i++) {
			
			if(attributes.get(i).isEquals(t)){
				pos=i;
				break;
			}
			
		}
		
		return pos;
		
	}
	
	public  ArrayList<String> getVarsSharedWith(Atom S){
		String att1=this.getVars();
		String att2=S.getVars();
		ArrayList<String> sharedVar=new ArrayList<String>();
		int size=att1.length();
		for (int i = 0; i < size; i++) {
			if(att2.indexOf(att1.charAt(i))!=-1)
				sharedVar.add(att1.substring(i,i+1));
		}
		
		return (sharedVar.size()>0)? sharedVar : null;
	}
	
	
	
	
	/**
	 * retourne les termes partagés avec l'atome S
	 * @param S : Atom
	 * @return liste de termes partagés avec S.
	 */
	public  ArrayList<Term> getTermSharedWith(Atom S){
		
		ArrayList<Term> sharedTerms=new ArrayList<Term>();
		int size=attributes.size();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < S.attributes.size(); j++) {
				if(attributes.get(i).isEquals(S.attributes.get(j)))
					sharedTerms.add(attributes.get(i));
			}
		}
		
		return (sharedTerms.size()>0)? sharedTerms : null;
	}


}
