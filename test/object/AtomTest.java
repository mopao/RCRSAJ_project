package object;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AtomTest {
    
	private Atom R,T;
	@Before
	public void setUp() throws Exception {
		R=new Atom("Vins(x;c,m)","x");
		T=new Atom("Abus(c,m;\"A\")","x"); 
	}

	@Test
	public void testGetKeySize() {
		assertTrue(R.getKeySize()==1);
		assertTrue(T.getKeySize()==2);
	}

	@Test
	public void testGetName() {
		assertTrue(R.getName().equals("Vins"));
		assertTrue(T.getName().equals("Abus"));
	}

	@Test
	public void testGetSize() {
		assertTrue(R.getSize()==3 && T.getSize()==3);
		
	}

	@Test
	public void testGetKey() {
		assertTrue(R.keys().size()==0);
		assertTrue(T.keys().contains("c"));
		assertTrue(T.keys().contains("m"));
	}

	@Test
	public void testGetVars() {
		assertTrue(R.getVars().length()==2);
		assertTrue(R.getVars().equals("cm"));
		assertTrue(T.getVars().length()==2);
		assertTrue(T.getVars().equals("cm"));
	}

	@Test
	public void testGetVarsSharedWith() {
		assertTrue(R.getVarsSharedWith(T).size()==2);
		assertTrue(R.getVarsSharedWith(T).contains("c"));
		assertTrue(R.getVarsSharedWith(T).contains("m"));
	}
	
	@Test
	public void testisEquals() {
		assertTrue(R.isEquals(R));
		assertFalse(R.isEquals(T));
	}

}
