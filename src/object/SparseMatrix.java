package object;

import java.util.ArrayList;

/**
 * il s'agit de la matrice creuse au format de Yale
 * @author franck
 *
 */
public class SparseMatrix {
	
 private int [] A,IA,JA;
 
 public SparseMatrix(AttackGraph g){
	 int n=g.getVertexes().size();
	 int m=g.getEdges().size();
	 A=new int[m];
	 IA=new int[n+1];
	 JA= new int[m];	 
	 int [] []matrix=g.getAdjacencyMatrix();
	 
	 initialisation(matrix, n, m);
	 
 }
 
 public SparseMatrix(Graph g){
	 int n=g.getVertexes().size();
	 int m=g.getEdges().size();
	 A=new int[2*m];
	 IA=new int[n+1];
	 JA= new int[2*m];	 
	 int [] []matrix=g.getAdjacencyMatrix();
	 
	 initialisation(matrix, n, m);
	 
 }
 
 private void initialisation(int [] []matrix, int n, int m){
	 int k=0, t=0;
	 for(int i=0;i<n;i++){
		 IA[t]=k;
		 for (int j = 0; j < n; j++) {
				
			 if(matrix[i][j]!=0){
				 A[k]=matrix[i][j];
				 JA[k]=j; 
				 k++;
				 
			 }
		}
		 t++;
	 }
	 IA[t]=k;
 }

 public int getEltAtIndex(int index){
	 return A[index];
 }
 
 public int getIndexFirstElt(int lign){
	 return IA[lign];
 }
 
 public int getIndexLastElt(int lign){
	 return IA[lign+1]-1;
 }
 
 public int getColumn(int indexElt){
	 return JA[indexElt];
 }

 
 public ArrayList<ArrayList<Integer>> getSomeLoops(){
		ArrayList<ArrayList<Integer>> cycles=new ArrayList<ArrayList<Integer>>();
		boolean isCurrentPath=false;
		int n=IA.length-1,m=A.length;
		ArrayList<Integer> visitedVertexes=new ArrayList<Integer> (n), visitedEdges=new ArrayList<Integer> (m);
		ArrayList<Integer>stack=new ArrayList<Integer>();
		
		for (int i = 0; i < n; i++) {
			visitedVertexes.add(0);
		}
		
		for (int i = 0; i < m; i++) {
			visitedEdges.add(0);
			
		}		
		
		while (visitedVertexes.contains(0)){
			stack.add(visitedVertexes.indexOf(0));
			visitedVertexes.set(stack.get(0), 1);
			while(stack.size()>0){
				int from =stack.get(0);
				boolean isneighbor=false;
				for (int i = getIndexFirstElt(from); i <= getIndexLastElt(from); i++) {				
					if(visitedVertexes.get(getColumn(i))==0){
						stack.add(0, getColumn(i));
						visitedEdges.set(i, 1);
						visitedVertexes.set(getColumn(i),1);
						isCurrentPath=true;
						isneighbor=true;
						break;
					}
					else if(visitedVertexes.get(getColumn(i))==2 && 
							visitedEdges.get(i)==0){
						isCurrentPath=true;
						isneighbor=true;
						stack.add(0, getColumn(i));
						visitedEdges.set(i, 1);
						break;
						
					}
					else if(visitedVertexes.get(getColumn(i))==2 && 
							visitedEdges.get(i)==1 &&
							isCurrentPath && 
							!stack.contains(getColumn(i))){
						
						isneighbor=true;
						stack.add(0, getColumn(i));
						break;
						
					}
					else if(visitedVertexes.get(getColumn(i))==1 && 
							(isCurrentPath|| visitedEdges.get(i)==0)){
						visitedEdges.set(i, 1);
						ArrayList<Integer>cycle=new ArrayList<Integer>();
						int start=stack.indexOf(getColumn(i));
						for (int j = start; j >=0; j--) {
							cycle.add(stack.get(j));
							
						}
						cycle.add(getColumn(i));
						cycles.add(cycle);
						
					}
					
					
					
					
				}
				
				if(!isneighbor){
					isCurrentPath=false;
					visitedVertexes.set(from,2);
					stack.remove(0);
				}
				
			}
		}
		
		return (cycles.size()>0)? cycles : null;
	}
 
 public ArrayList<Integer> topologicalSorting(){
	
		int n=IA.length-1,s;
		ArrayList<Integer> visitedVertexes=new ArrayList<Integer> (n);
		ArrayList<Integer>stack=new ArrayList<Integer>();
		ArrayList<Integer>listSorted=new ArrayList<Integer>();
		
		for (int i = 0; i < n; i++) {
			visitedVertexes.add(0);
		}
		
		do {
		    s=-1;
			for (int i = 0; i < IA.length-1; i++) {
				int j=0;
				for (; j < JA.length; j++) {
					
					if(i==getColumn(j))
						break;
				}
				if(j>= JA.length && visitedVertexes.get(i)==0){
					s=i;
					stack.add(0,s);
					visitedVertexes.set(s,1);
					break;
				}
					
				
			}
			
			while(!stack.isEmpty()){
				int from=stack.get(0);
				int i = getIndexFirstElt(from);
				for (; i <= getIndexLastElt(from); i++) {				
					if(visitedVertexes.get(getColumn(i))==0){
						stack.add(0, getColumn(i));
						visitedVertexes.set(getColumn(i),1);						
						break;
					}
				}
				if(i > getIndexLastElt(from)){
					stack.remove(0);
					visitedVertexes.set(from,2);
					listSorted.add(0,from);
					
				}
			}
			
			
			
		} while (s!=-1);
		
		return listSorted;
	 
 }
 
 /**
  * 
  * @return retourne la liste de tous les sommets accessible à partir du sommet s.
  */
 public ArrayList<Integer> bfs(int s){
	 
	 ArrayList<Integer>queue=new ArrayList<>();
	 ArrayList<Integer> joinVertex=new ArrayList<>();
	 
	 queue.add(s);
	 int [] visited=new int[IA.length-1];
	 for (int i = 0; i < visited.length; i++) {
		 visited[i]=0;
	}
	 visited[s]=1;
	 while(!queue.isEmpty()){
		 int v=queue.get(0);
		 queue.remove(0);
		 for (int i = getIndexFirstElt(v); i <= getIndexLastElt(v); i++) {
			if(visited[getColumn(i)]==0){
				visited[getColumn(i)]=1;
				queue.add(getColumn(i));
				joinVertex.add(getColumn(i));
			}
		}
	 }
	 
	 return (joinVertex.size()>0)? joinVertex:null;
 }
 
 
}
