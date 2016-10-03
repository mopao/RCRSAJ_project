package object;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TermTest {
	private Atom R;

	@Before
	public void setUp() throws Exception {
		R=new Atom("Vins(x;c,\"1986\")","x");
	}

	@Test
	public void testIsInHead() {
		assertTrue(R.getAttributes().get(0).isInHead());
		assertFalse(R.getAttributes().get(1).isInHead());
		assertFalse(R.getAttributes().get(2).isInHead());
	}

	@Test
	public void testGetType() {
		assertTrue(R.getAttributes().get(0).getType()==TermType.VAR);
		assertTrue(R.getAttributes().get(1).getType()==TermType.VAR);
		assertTrue(R.getAttributes().get(2).getType()==TermType.CONST);
	}

	@Test
	public void testIsEquals() {
		assertFalse(R.getAttributes().get(1).isEquals(R.getAttributes().get(0)));
		assertFalse(R.getAttributes().get(1).isEquals(R.getAttributes().get(2)));
		assertTrue(R.getAttributes().get(1).isEquals(R.getAttributes().get(1)));
	}
	
	@Test
	public void testIsElt() {
		assertTrue(R.getAttributes().get(1).getElt().equals("c"));
		assertTrue(R.getAttributes().get(0).getElt().equals("x"));
		assertTrue(R.getAttributes().get(2).getElt().equals("1986"));
	}

}
