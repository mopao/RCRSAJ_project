package object;
/**
 * cette classe représente un schéma de relation
 */
import java.util.ArrayList;

public class RelationSchema {
	
	private String relationName;
	private ArrayList<String> attributes;
	private ArrayList<String> typesOfAttributes;
	private int keysize;
	
	
	
	public RelationSchema(String relationName, ArrayList<String> attributes,
			ArrayList<String> typeAttributes, int keysize) {
		super();
		this.relationName = relationName;
		this.attributes = attributes;
		this.typesOfAttributes = typeAttributes;
		this.keysize = keysize;
	}

	
	public int getSize(){
		return attributes.size();
	}
	
	public String getRelationName() {
		return relationName;
	}
	/**
	 * 
	 * @param index
	 * @return retourne l'attribut à la position index dans le schéma de relation 
	 */
	public String getAttributAtIndex(int index){
		return (index>=0 && index<attributes.size())? attributes.get(index):null;
		
	}
	
	public String getTypeAttributAtIndex(int index){
		return (index>=0 && index<attributes.size())? typesOfAttributes.get(index):null;
		
	}
	
	/**
	 * 
	 * @return retourne les attributs qui forment la clé de la relation.
	 */
	public ArrayList<String> getkeyAttributes(){
		ArrayList<String> list=new ArrayList<String>();
		for (int i = 0; i < keysize; i++) {
			list.add(attributes.get(i));
		}
		return list;
	}

}
