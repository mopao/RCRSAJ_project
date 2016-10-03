/**
 * 
 */
package object;

/**
 * @author franck
 *
 */
public class Vertex {
	
	private String tag;
	private Atom content;
	private int id ;
	
	public int getId() {
		return id;
	}

	public Vertex(Atom R, int id){
		tag=R.getName();
		content=R;
		this.id=id;
	}

	public String getTag() {
		return tag;
	}

	public Atom getContent() {
		return content;
	}
	
	public boolean isEquals(Vertex v){
		return tag.equals(v.tag) && content.isEquals(v.content);
	}
	
	public String toString(){
		return tag;
	}

}
