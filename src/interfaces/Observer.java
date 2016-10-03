package interfaces;

import java.util.ArrayList;


public interface Observer {
	ArrayList<Observable> objs=new ArrayList<>();
	public void addObservable(Observable ob);

}
