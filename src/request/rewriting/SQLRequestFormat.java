package request.rewriting;

import java.util.ArrayList;

import object.Atom;
import object.AttackGraph;
import object.ConjonctiveRequest;
import object.Term;
import object.TermType;
import factory.BadRequestException;

public class SQLRequestFormat {
	
	private ArrayList<SQLCondition> ca ,ct;
	private ArrayList<SQLOperand> h;
	private ArrayList<Atom> sortedAtoms;
	
	
    public void setCa(ArrayList<SQLCondition> ca) {
		this.ca = ca;
	}

	public void setCt(ArrayList<SQLCondition> ct) {
		this.ct = ct;
	}

	public void setH(ArrayList<SQLOperand> h) {
		this.h = h;
	}

	public void setSortedAtoms(ArrayList<Atom> sortedAtoms) {
		this.sortedAtoms = sortedAtoms;
	}

	public SQLRequestFormat(ConjonctiveRequest q){
		ca=new ArrayList<SQLCondition>();
		ct=new ArrayList<SQLCondition>();
		h=new ArrayList<SQLOperand>();
		if(q.getHead().size()==0)
			initBoolRequest(q);
		else
			initNotBoolRequest(q);
	}
	
	/**
	 * met à jour les conditions (ca et ct) de l'écriture en sql de la requête booléenne q
	 * @param q : requête conjonctive booléenne
	 */
	private void initBoolRequest(ConjonctiveRequest q){
		
		for (int i = 0; i < q.getAtoms().size(); i++) {
			Atom R=q.getAtoms().get(i);
			for (int j = i+1; j < q.getAtoms().size(); j++) {
				Atom T=q.getAtoms().get(j);
				ArrayList<Term> termShared=R.getTermSharedWith(T);
				if(termShared!=null)
					for (Term term : termShared) {
						int index1=R.indexAt(term);
						int index2=T.indexAt(term);
						SQLCondition sqlcond1=new SQLCondition(
								new SQLOperand(R.getName(), R.getSchema().getAttributAtIndex(index1), R.getSchema().getTypeAttributAtIndex(index1), term.getType()),
								new SQLOperand(T.getName(), T.getSchema().getAttributAtIndex(index2), T.getSchema().getTypeAttributAtIndex(index2), term.getType()), 
								Operator.EQUALS);
						ca.add(sqlcond1);
						
					}
				
				
			}
			
			for (Term term : R.getAttributes()) {
				if(term.getType()==TermType.CONST){
					int index= R.indexAt(term);
					SQLCondition sqlcond2=new SQLCondition(new SQLOperand(R.getName(), R.getSchema().getAttributAtIndex(index), R.getSchema().getTypeAttributAtIndex(index), TermType.VAR),
							new SQLOperand(R.getName(), term.getElt(), R.getSchema().getTypeAttributAtIndex(index), term.getType()),
							Operator.EQUALS);
					ct.add(sqlcond2);
					
				}
				
			}
			
			
		} 
		try {
			sortedAtoms=new AttackGraph(q).topologicalSorting();
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//VINS(y,z;"A"),ABUS(x,y,z;)
	
	//1-  DIFFUSER(s,v,d,h;t),FILMS(t;r,y,u),ROLES(t,p;a,"Secondaire"),ACTEURS(a;w,"F")
	// 2- a<-- DIFFUSER(s,v,d,h;t),FILMS(t;r,y,u),ROLES(t,p;a,"Secondaire"),ACTEURS(a;w,"F")
	//3- d,h<--DIFFUSER(s,v,d,h;t),FILMS(t;r,y,u),ROLES(t,p;a,"Principal"),ACTEURS(a;w,"M")
	//4- t,b,y,u<- PRODUCTIONS(t,c;b),FILMS(t;r,y,u),ROLES(t,p;a,q),ACTEURS(a;w,"M")
	//PRODUCTIONS(t,c;b),FILMS(t;r,y,u),DIFFUSER(s,v,d,h;t),ROLES(t,p;a,q),	ACTEURS(a;w,"M")
	
	/**
	 * met à jour les conditions (ca et ct) et le résultat (h)de l'écriture en sql de la 
	 * requête non booléenne q
	 * @param q requête conjonctive booléenne
	 */
	private void initNotBoolRequest(ConjonctiveRequest q){
		
		ArrayList<String> head=q.getHead();
		for (String var : head) {
			for (Atom atom  : q.getAtoms()) {
				int index=atom.indexAt(new Term(var, TermType.VAR));
				if(index!=-1){
					h.add(new SQLOperand(atom.getName(), atom.getSchema().getAttributAtIndex(index), 
							atom.getSchema().getTypeAttributAtIndex(index), TermType.VAR));
					break;
				}
			}
			
		}
		
		initBoolRequest(q);
		
	}
	
	

	public ArrayList<SQLCondition> getCa() {
		return ca;
	}

	public ArrayList<SQLCondition> getCt() {
		return ct;
	}

	public ArrayList<SQLOperand> getH() {
		return h;
	}

	public ArrayList<Atom> getSortedAtoms() {
		return sortedAtoms;
	}
	

}
