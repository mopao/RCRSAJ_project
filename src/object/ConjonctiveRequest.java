package object;

import interfaces.Observable;
import interfaces.Observer;

import java.util.ArrayList;
import view.RecapPanel;

import factory.BadRequestException;

public class ConjonctiveRequest implements Observer{
	
	private ArrayList<Atom> atoms;		
	private ArrayList<String> head;
	
	public ArrayList<String> getHead() {
		return head;
	}


	public ArrayList<Atom> getAtoms() {
		return atoms;
	}


	public ArrayList<DF> df() {
		ArrayList<DF> list_df=new ArrayList<DF>();
		for (Atom atom : atoms) {
			try {
				list_df.add(atom.df());
			} catch (BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list_df;
	}


	public ConjonctiveRequest(ArrayList<Atom> listAtoms, String h) throws BadRequestException{
		
		
		int size=listAtoms.size();
		atoms= new ArrayList<Atom>(size);
		for (int j = 0; j < size; j++) {
			atoms.add(listAtoms.get(j));
			if(j>0){
				
				
				if(atoms.get(j-1).getTermSharedWith(atoms.get(j))==null)
					throw new BadRequestException("Two consecutive atoms request must share at least one variable .");
				
				
			}
			
		}
		
		head=new ArrayList<String>();
		if(!h.isEmpty()){
			verifyHead(h);
			for (int i = 0; i < h.length(); i++) {
				head.add(h.substring(i, i+1));
			}
		}
				
		
		
		
	}
	
	/**
	 * contrôle si chaque variable de la tête de la requête apparait dans un atome
	 * de corps de la requête
	 * @param head : liste de variables de la tête
	 * @throws BadRequestException
	 */
	private void verifyHead(String head)throws BadRequestException{
		
		int i=0; boolean find=false;
		while(i<head.length()){
			for (Atom atom : atoms) {
	    		
	    		for (Term term : atom.getAttributes()) {
	    			if(term.getType()==TermType.VAR && head.substring(i, i+1).equals(term.getElt())){
	    				find=true;
	    				break;
	    			}
					
				}
	    		if(find)
	    			break;
	    	}
			if(!find)
				throw new BadRequestException("The variable "+head.substring(i, i+1)+" in the head does not appear in the body.");
				
			i+=1;
			find=false;
			
		}
    	
    	
    }
	
	/**
	 * 
	 * @param R
	 * @return
	 */
	
	public ArrayList<String> externalDependencies(Atom R) throws BadRequestException{
		
		ArrayList<DF> newDf=removeDependencyOf(R);
		return dependencies(newDf, R);
		
	}
	
	public ArrayList<String> dependencies(ArrayList<DF> list_df,Atom R) throws BadRequestException{
		
		ArrayList<String> results=new ArrayList<String>();
		ArrayList<DF> newDf= new ArrayList<>(list_df);
		
		for (int i = 0; i < R.keys().size(); i++) {
			results.add(R.keys().get(i));
		}
		
        boolean change=true, isKeyInside;
		
		while(change){
			int  lastSize=results.size();
			int i = 0;
			while (  i< newDf.size())  {
				isKeyInside=true;
				for (int j = 0; j < newDf.get(i).getKey().length(); j++) {
					if(!results.contains(String.valueOf(newDf.get(i).getKey().charAt(j)))){
						isKeyInside=false;
					    break;
					}
						
				}
				if(isKeyInside){
					
					for (int j = 0; j < newDf.get(i).getDependency().length(); j++) {
						if(!results.contains(String.valueOf(newDf.get(i).getDependency().charAt(j))))
							results.add(newDf.get(i).getDependency().substring(j, j+1));							
					}
					newDf.remove(newDf.get(i));
					
					
				}
				else
					i++;
				
			}
			
			if(lastSize == results.size())
				change=false;
		}
		
		return results;
	}
	
	
	private ArrayList<DF> removeDependencyOf(Atom R)throws BadRequestException{
		
			ArrayList<DF> newDf= df();
			for (DF df : newDf) {
				if(df.isEquals(R.df())){
					newDf.remove(df);
					break;
				}
				
			}
				
			return newDf;
		}
	
	/**
	 * retourne la liste d'atomes attaqués par l'atome R
	 * @param R: un atome
	 * @return retourne la liste d'atomes attaqués par l'atome R, ou null
	 * @throws BadRequestException 
	 */
	public ArrayList<Integer> getIndexAttackedAtoms(Atom R) throws BadRequestException{
		
		Graph g=new Graph(this, R);
		SparseMatrix sparseMatrix=new SparseMatrix(g);
		ArrayList<Integer>joinVert=sparseMatrix.bfs(atoms.indexOf(R));		
		
		return (joinVert!=null&&joinVert.size()>0)? joinVert:null;
	}
	
	
	
	public String toString(){
		String hd="";
		for (int i=0;i<head.size();i++) {
			if(i==head.size()-1)
				hd+=head.get(i);
			else
				hd+=head.get(i)+",";
		}
		String result="q : ans("+hd+") <-- ";
		for (int i = 0; i < atoms.size(); i++) {
			
			result+=atoms.get(i).toString();
			if(i< atoms.size()-1)
				result+=",";				
			
		}
		
		return result;
	}


	


	@Override
	public void addObservable(Observable obj) {
		// TODO Auto-generated method stub
		objs.add(obj);
		for (Observable ob : objs) {
					
					if(ob instanceof RecapPanel){
						ob.setView(toString());
						
					}
		}
	}

}
