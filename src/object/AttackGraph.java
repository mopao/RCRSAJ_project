/**
 * 
 */
package object;

import interfaces.Observable;
import interfaces.Observer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.text.View;

import org.apache.commons.collections15.Transformer;

import view.ComplexityPanel;
import view.DetailsPanel;
import view.RecapPanel;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ViewScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import factory.BadRequestException;

/**
 * @author franck
 *
 */
public class AttackGraph implements Observer{
	
	private ArrayList<Vertex> vertexes;
	private ArrayList<OrientedEdge> edges;	
	private HashMap<ArrayList<OrientedEdge>,EdgeKind>cycles=null;
	private boolean isThereStrongCycle= false;
	
	public ArrayList<Vertex> getVertexes() {
		return vertexes;
	}
	public ArrayList<OrientedEdge> getEdges() {
		return edges;
	}
	public HashMap<ArrayList<OrientedEdge>, EdgeKind> getCycles() {
		return cycles;
	}

	
	
	public AttackGraph(ConjonctiveRequest q)throws BadRequestException{
		
		vertexes=new ArrayList<Vertex>();		
		for(int i=0;i<q.getAtoms().size();i++)
			vertexes.add(new Vertex(q.getAtoms().get(i),i+1));
		
		makeEdges(q);
		detectSomeLoops();
		
		
	}
	
	
	/**
	 * créé les arcs du graphe d'attaque
	 * @param q: reuête conjonctive
	 * @throws BadRequestException
	 */
	public void makeEdges(ConjonctiveRequest q)throws BadRequestException{
		
		edges=new ArrayList<OrientedEdge>();		
		ArrayList<DF> deps=q.df();
		for (Vertex a : vertexes) {
			ArrayList<Integer>indexAttackedAtoms=q.getIndexAttackedAtoms(a.getContent());
			if(indexAttackedAtoms!=null)
				for(int index : indexAttackedAtoms){
					
					EdgeKind type=EdgeKind.STRONG;
					ArrayList<String> a_dependencies=q.dependencies(deps,a.getContent());
					ArrayList<String> b_key=vertexes.get(index).getContent().keys();
					if(a_dependencies.containsAll(b_key))
						type=EdgeKind.WEAK;
					edges.add(new OrientedEdge(a , vertexes.get(index), type));
				
			    }
			
		}
	}
	
	public ArrayList<Atom> topologicalSorting(){
		if(isFOComplexity()){
			SparseMatrix sp=new SparseMatrix(this);
			ArrayList<Integer>orders=sp.topologicalSorting();
			ArrayList<Atom> sortedList=new ArrayList<>();
			int i=0;
			while(i<orders.size()){
				for (Vertex v: vertexes) {
					if(v.getId()-1==orders.get(i)){
						sortedList.add(v.getContent());
						i++;
						break;
					}
					
				}
				
			
				
			}
			return (sortedList.size()>0)? sortedList:null;
		}
		return null;
	}
	/**
	 * 
	 * @return matrice d'adjacence du graphe d'attaque
	 */
	public int [][] getAdjacencyMatrix(){
		
		int n=vertexes.size();
		int [] [] matrix=new int [n][n];
		/* initialisation de la matrice */
		for(int i=0; i<n;i++)
			for (int j = 0; j < n; j++) 
				matrix[i][j]=0;				
			
		
		/* remplissage de la matrice */
		for (OrientedEdge edge : edges) {
			matrix[edge.getFrom().getId()-1][edge.getTo().getId()-1]=1;
			
		}
		return matrix;
	}
	
	/**
	 * recherche les cycles dans le graphe d'attaque.
	 */
	public  void detectSomeLoops(){
		SparseMatrix p=new SparseMatrix(this);
		ArrayList<ArrayList<Integer>> cycles_b=p.getSomeLoops();
		if(cycles_b!=null){
			int nberCycles=cycles_b.size();
			cycles=new HashMap<ArrayList<OrientedEdge>, EdgeKind>();
			for (int i = 0; i < nberCycles; i++) {
				ArrayList<Integer> cycle_b=cycles_b.get(i);
				int sizeCycle=cycles_b.get(i).size();	
				ArrayList<OrientedEdge> cycle= new ArrayList<OrientedEdge>(sizeCycle-1);
				EdgeKind typeCycle=EdgeKind.WEAK;
				for (int j = 0; j < sizeCycle-1; j++) {
					for (OrientedEdge edge: edges) {
						if(edge.getFrom().getId()-1== cycle_b.get(j) &&
						   edge.getTo().getId()-1== cycle_b.get(j+1)){							
							cycle.add(edge);
							if(edge.isStrong())
								typeCycle=EdgeKind.STRONG;
								
							break;
						}
						
					}
				}
				
				if(typeCycle==EdgeKind.STRONG)
					isThereStrongCycle=true;
				
				cycles.put(cycle, typeCycle);
				
			}
		}
		
	}
	
	public boolean isContainsCycle(){
		
		return(cycles==null)? false:true;
	}
	
	public boolean isContainsStrongCycle(){		
		
			return isThereStrongCycle;
			
			
		}
	
	public String toStringCycle(ArrayList<OrientedEdge> cycle){
		
		String result="";
		for (int i=0 ; i<cycle.size(); i++) {				
			    
			if(i==0)
				result+=cycle.get(0).getFrom().getTag()+"-->"+cycle.get(0).getTo().getTag();
			else
				result+="-->"+cycle.get(i).getTo().getTag();
			
		}
		return result;
	}
	
	
	
	public String toString(){
		String result="le graphe d'attaque g = "+vertexes.size()+" Sommets : ";
		
		for (int i=0 ; i<vertexes.size(); i++) {
			
			if(i==vertexes.size()-1)
				result+=vertexes.get(i).toString();
			else
				result+=vertexes.get(i).toString()+",";
			
		}
		result+="\n "+edges.size()+" Arcs : ";
		for (int i=0 ; i<edges.size(); i++) {
					
					if(i==edges.size()-1)
						result+=edges.get(i).toString();
					else
						result+=edges.get(i).toString()+",";
					
				}
		
		return result;
		
	}
	
	
	public boolean isFOComplexity(){
	     return cycles==null;
	}
	
	public boolean isPComplexity(){
		return !isFOComplexity() && !isContainsStrongCycle();
	}
	
	public boolean isNPComplexity(){
		return !isFOComplexity() && !isPComplexity();
	}
	@Override
	public void addObservable(Observable obj) {
		// TODO Auto-generated method stub
		objs.add(obj);
		for (Observable ob : objs) {
			
			if(ob instanceof ComplexityPanel){
				int compl=-1;
				if(isFOComplexity())
					compl=0;
				else if(isPComplexity())
					compl=1;
				else
					compl=2;
				
				ob.setView(compl);
					
			}
			
			else if(ob instanceof DetailsPanel){
				
				ob.setView(this);
				
			}
				
		}
	}
	

}
