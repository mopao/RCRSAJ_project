package object;

import java.util.ArrayList;

import factory.BadRequestException;

/**
 * représente un graphe non orienté et non valué avec
 * des entiers comme sommets
 * @author franck
 *
 */
public class Graph {
	
	private ArrayList<Integer> vertexes ;
	private ArrayList<Edge> edges;
	public Graph(ConjonctiveRequest q, Atom R) throws BadRequestException{
		vertexes=new ArrayList<>();
		edges=new ArrayList<>();
		int sizeVertex=q.getAtoms().size();
		for (int i = 0; i < sizeVertex; i++) {
			vertexes.add(i);
		}
		ArrayList<String>varsExternDep=q.externalDependencies(R);
		
		for (int i = 0; i < sizeVertex; i++) {
			for (int j = i+1; j < sizeVertex; j++) {
				ArrayList<String>vars=q.getAtoms().get(i).getVarsSharedWith(q.getAtoms().get(j));
				if(vars!=null && !varsExternDep.containsAll(vars)){
					edges.add(new Edge(i, j));
				}
				
			}
		}
		
	}
	
	
	/**
	 * 
	 * @return matrice d'adjacence du graphe
	 */
	public int [][] getAdjacencyMatrix(){
		int n=vertexes.size();
		int [] [] matrix=new int [n][n];
		/* initialisation de la matrice */
		for(int i=0; i<n;i++)
			for (int j = 0; j < n; j++) 
				matrix[i][j]=0;
		
		ArrayList<Edge> listedges=new ArrayList<Edge>(edges);
		/* remplissage de la matrice */
		int i=0;
		while(i<n && !listedges.isEmpty()){
			int j=0;
			while( j<listedges.size()){
				if(listedges.get(j).getV1()==vertexes.get(i)||
						listedges.get(j).getV2()==vertexes.get(i)){
					if(listedges.get(j).getV1()==vertexes.get(i)){
						matrix[vertexes.get(i)][listedges.get(j).getV2()]=1;
						matrix[listedges.get(j).getV2()][vertexes.get(i)]=1;
					}
						
					else{
						matrix[vertexes.get(i)][listedges.get(j).getV1()]=1;
						matrix[listedges.get(j).getV1()][vertexes.get(i)]=1;
						
					}
						
					
					listedges.remove(j);
					
						
				}
				else
					j++;
				
			}
			i++;
		}
		
		return matrix;
	}

    
	public ArrayList<Integer> getVertexes() {
		return vertexes;
	}


	public void setVertexes(ArrayList<Integer> vertexes) {
		this.vertexes = vertexes;
	}


	public ArrayList<Edge> getEdges() {
		return edges;
	}


	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}


	
	
}
