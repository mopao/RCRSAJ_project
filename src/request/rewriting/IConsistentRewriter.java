package request.rewriting;

import java.util.ArrayList;


/**
 * cette classe représente un réécriveur consistent de requête conjonctive sans auto-jointure
 * @author franck
 *
 */
public interface IConsistentRewriter {
	
	
	/**
	 * réécrit une requête booléenne 
	 * @return  la requête réécrite de façon consistente
	 */
	public String boolRequestRewriting(SQLRequestFormat formattedRequest,ArrayList<String>list_atoms);
	
	/**
	 * réécrit une requête non booléenne 
	 * @return la requête réécrite de façon consistente
	 */
	public String notBoolRequestRewriting(SQLRequestFormat formattedRequest,ArrayList<String>list_atoms);
	

}
