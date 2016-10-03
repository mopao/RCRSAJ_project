package request.rewriting;
/**
 * cette classe représente un réécriveur consistent sql
 */
import java.util.ArrayList;

import object.Atom;
import object.TermType;

public class SQLConsistentRewriter implements IConsistentRewriter{

	private String tabulation;
	public SQLConsistentRewriter() {
		super();
		// TODO Auto-generated constructor stub
		tabulation="";
	}

	/**
	 * renvoie les conditions de ca dont les noms de relations sont dans relationNames
	 * @param relationNames : liste de noms de relations
	 * @return liste de conditions SQL
	 */
	private ArrayList<SQLCondition> getCAConditions(ArrayList<SQLCondition>ca,ArrayList<String> relationNames){
		
		ArrayList<SQLCondition>conditions=new ArrayList<>();
		for (SQLCondition sqlCondition : ca) {
			if(relationNames.contains(sqlCondition.getLeftOprerand().getRelationName()) &&
					relationNames.contains(sqlCondition.getRightOperand().getRelationName()))
				conditions.add(sqlCondition);
			
			
		}
		
		return (conditions.size()>0)? conditions:null;
	}
	
	/**
	 * renvoie les conditions de ct dont les noms de relations sont dans relationNames
	 * @param relationNames : liste de noms de relations
	 * @return liste de conditions SQL
	 */
	private ArrayList<SQLCondition> getCTConditions(ArrayList<SQLCondition>ct,ArrayList<String> relationNames){
		ArrayList<SQLCondition>conditions=new ArrayList<>();
		for (SQLCondition sqlCondition : ct) {
			if(relationNames.contains(sqlCondition.getLeftOprerand().getRelationName()))
				conditions.add(sqlCondition);
			
			
		}
		
		return (conditions.size()>0)? conditions:null;
	}
	
	/**
	 * supprime les conditions de ca dont les noms de relations sont dans relationNames
	 * @param relationNames : liste de noms de relations
	 */
	private ArrayList<SQLCondition> removeCAConditions(ArrayList<SQLCondition>ca,ArrayList<String> relationNames){
		
		ArrayList<SQLCondition>new_ca=new ArrayList<SQLCondition>();
		for (SQLCondition sqlCondition : ca) {
			if(!relationNames.contains(sqlCondition.getLeftOprerand().getRelationName()) ||
					!relationNames.contains(sqlCondition.getRightOperand().getRelationName()))
				new_ca.add(sqlCondition);
		}
		
		return new_ca;
		
	}
	
	/**
	 * supprime les conditions de ct dont les noms de relations sont dans relationNames
	 * @param relationNames : liste de noms de relations
	 */
	private ArrayList<SQLCondition> removeCTConditions(ArrayList<SQLCondition>ct,ArrayList<String> relationNames){
		ArrayList<SQLCondition>new_ct=new ArrayList<SQLCondition>();
		for (SQLCondition sqlCondition : ct) {
			if(!relationNames.contains(sqlCondition.getLeftOprerand().getRelationName()))
				new_ct.add(sqlCondition);
		}
		return new_ct;	
	}

	@Override
	public String boolRequestRewriting(SQLRequestFormat formattedRequest,ArrayList<String>list_atoms) {
		if(formattedRequest.getSortedAtoms().size()>0){
			Atom R=formattedRequest.getSortedAtoms().get(0);
			if(formattedRequest.getSortedAtoms().size()==1 && formattedRequest.getCa().size()==0&& formattedRequest.getCt().size()==0)
				return "SELECT \'true\' FROM "+R.getName();
			formattedRequest.getSortedAtoms().remove(R);
			list_atoms.add(R.getName());
			ArrayList<String>R_keys=R.getSchema().getkeyAttributes();
			ArrayList<SQLOperand>R_operandKeys=new ArrayList<>();
			for (String key : R_keys) {
				R_operandKeys.add(new SQLOperand(R.getName(), key, "varchar", TermType.VAR));
			}
			int index1=1, index2=2;
			String keyConditions="";
			for (int i=0; i<R_operandKeys.size();i++) {
				SQLOperand leftOp=R_operandKeys.get(i).getRenamedOperand(index1);
				SQLOperand rightOp=R_operandKeys.get(i).getRenamedOperand(index2);
				if(i>0)
					keyConditions+=" AND ";
				keyConditions+=new SQLCondition(leftOp, rightOp, Operator.EQUALS).toString();
			}
			ArrayList<SQLCondition>ca_conditions=getCAConditions(formattedRequest.getCa(),list_atoms);
			ArrayList<SQLCondition>ct_conditions=getCAConditions(formattedRequest.getCt(),list_atoms);
			String conditions="";
			if(formattedRequest.getSortedAtoms().size()==0){
				if(ca_conditions!=null)
					for (int i=0; i<ca_conditions.size();i++) {			
						if(i>0)
							conditions+=" AND ";
						conditions+=ca_conditions.get(i).getRenamedCondition(index2).toString();
					}
				if(!conditions.isEmpty() && ct_conditions!=null)
					conditions+=" AND ";
				if(ct_conditions!=null)
					for (int i=0; i<ct_conditions.size();i++) {			
						if(i>0)
							conditions+=" AND ";
						conditions+=ct_conditions.get(i).getRenamedCondition(index2).toString();
					}
				formattedRequest.setCa(removeCAConditions(formattedRequest.getCa(), list_atoms));
				formattedRequest.setCt(removeCTConditions(formattedRequest.getCt(), list_atoms));
			}
			String request= "SELECT \'true\' FROM "+R.getName()+" AS "+R.getName()+index1+" WHERE NOT EXISTS(\n"+tabulation +
					"SELECT \'true\' FROM "+R.getName()+" AS "+R.getName()+index2+" WHERE "+keyConditions+" ";
			
			if(!conditions.isEmpty()){
				conditions= "\n"+tabulation+"AND NOT("+conditions+")";
				request+=conditions;
			}
			if(formattedRequest.getSortedAtoms().size()==0){
				tabulation="";
				return request+")";								
			}
			
			/*if(!conditions.isEmpty() && formattedRequest.getSortedAtoms().size()>0)				
				request+=" OR ";
			else
				request+=" AND ";*/
				
			tabulation+="   ";	
	        return request+" AND NOT EXISTS(\n"+tabulation+boolRequestRewriting(formattedRequest, list_atoms)+"))";
		}
		
		return "";
	}

	@Override
	public String notBoolRequestRewriting(SQLRequestFormat formattedRequest,ArrayList<String>list_atoms) {
		// TODO Auto-generated method stub
		int index=0;
		String answer="";
		String table_answer="";
		ArrayList<String> relations=new ArrayList<>();
		for (int i=0 ; i<formattedRequest.getH().size();i++) {
			SQLOperand op=formattedRequest.getH().get(i).getRenamedOperand(index);
			formattedRequest.getCa().add(new SQLCondition(formattedRequest.getH().get(i), op, Operator.EQUALS));
			if(i>0){
				answer+=",";
			}
				
			answer+=op.toString();
			if(!relations.contains(formattedRequest.getH().get(i).getRelationName())){
				if(i>0){					
					table_answer+=",";
				}
				table_answer+=formattedRequest.getH().get(i).getRelationName()+" AS "+formattedRequest.getH().get(i).getRelationName()+index;
				relations.add(formattedRequest.getH().get(i).getRelationName());
			}
				
			
		}
		tabulation="   ";
		return "SELECT DISTINCT "+answer+" FROM "+table_answer+" WHERE EXISTS(\n"+tabulation
		+boolRequestRewriting(formattedRequest, list_atoms)+")";
	}
	
	
	
	

}
