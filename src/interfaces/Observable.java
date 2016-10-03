package interfaces;

import object.AttackGraph;

public interface Observable {
	
	public void setView(int compl);
	public void setView(String request);
	public void setView(AttackGraph g);

}
