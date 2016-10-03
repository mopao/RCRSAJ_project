package request.rewriting;

import object.TermType;

/**
 * cette classe représente une opérande d'une condition en sql
 * @author franck
 *
 */
public class SQLOperand {

	private String relationName, attribute, typeAttribute;
    private TermType type ;	
	private int index=-1;
    
	public boolean isRenamed() {
		return index!=-1;
	}
	public void setRenamed(int index) {
		this.index = index;
	}
	public SQLOperand(String relationName, String attribute,
			String typeAttribute, TermType type) {
		super();
		this.relationName = relationName;
		this.attribute = attribute;
		this.typeAttribute = typeAttribute;
		this.type = type;
	}
	/**
	 * renomme l'opérande
	 * @param number : numéro de renommage de l'opérande
	 * @return opérande renommée
	 */
	public SQLOperand getRenamedOperand(int number){
		if(!isRenamed()){
			SQLOperand op= new SQLOperand(relationName, attribute, typeAttribute, type);
			op.setRenamed(number);
			return op;
		}
		return this;
	}
	
	
	
	public String getRelationName() {
		return relationName;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getTypeAttribute() {
		return typeAttribute;
	}

	public TermType getType() {
		return type;
	}

	public String toString(){
		String result=null;
		if(type==TermType.CONST){
			if(typeAttribute.toLowerCase().contains("char")||
					typeAttribute.toLowerCase().contains("text"))
				result="\'"+attribute+"\'";
			else
				result=attribute;
		}
		
		else{
			if(!isRenamed())
				result=relationName+"."+attribute;
			else
				result=relationName+index+"."+attribute;
				
			
		}
			
		
		return result;
		
	}
}
