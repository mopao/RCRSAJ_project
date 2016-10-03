/**
 * 
 */
package object;

/**
 * @author franck
 *
 */
public class OrientedEdge {
	
	private Vertex from, to;
	private EdgeKind weight;
	
	public EdgeKind getWeight() {
		return weight;
	}

	public void setWeight(EdgeKind weight) {
		this.weight = weight;
	}

	public Vertex getFrom() {
		return from;
	}

	public Vertex getTo() {
		return to;
	}

	public OrientedEdge(Vertex from, Vertex to, EdgeKind weight) {
		super();
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public boolean isWeak(){
		return weight==EdgeKind.WEAK;
	}
	
	public boolean isStrong(){
		return weight==EdgeKind.STRONG;
	}
	
	public String toString(){
		return "("+from.toString()+", "+ to.toString()+")";
	}

}
