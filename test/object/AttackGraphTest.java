package object;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import factory.RequestParser;

public class AttackGraphTest {
	private AttackGraph g1,g2;

	@Before
	public void setUp() throws Exception {
		g1=new AttackGraph(new RequestParser().getConjonctiveRequest("Abus(x;c,m),Vins(c,m;\"A\")", "x"));
		g2=new AttackGraph(new RequestParser().getConjonctiveRequest("R(x; y), G (y ;z), B(z;x), U(x;u), V (x, u; v)", ""));
	}

	@Test
	public void testGetVertexes() {
		assertTrue(g1.getVertexes().size()==2);
		assertTrue(g2.getVertexes().size()==5);
	}

	@Test
	public void testGetEdges() {
		assertTrue(g1.getEdges().size()==1);
		assertTrue(g2.getEdges().size()==11);
	}

	@Test
	public void testTopologicalSorting() {
		assertNull(g2.topologicalSorting());
		assertTrue(g1.topologicalSorting().size()==2);
		assertTrue(g1.topologicalSorting().get(0).getName().equals("Abus"));
		assertTrue(g1.topologicalSorting().get(1).getName().equals("Vins"));
	}

	@Test
	public void testIsContainsCycle() {
		assertFalse(g1.isContainsCycle());
		assertTrue(g2.isContainsCycle());
	}

	@Test
	public void testIsContainsStrongCycle() {
		assertFalse(g1.isContainsStrongCycle());
		assertFalse(g2.isContainsStrongCycle());
	}

	@Test
	public void testIsFOComplexity() {
		assertTrue(g1.isFOComplexity());
		assertFalse(g2.isFOComplexity());
	}

	@Test
	public void testIsPComplexity() {
		assertTrue(g2.isPComplexity());
		assertFalse(g1.isPComplexity());
	}

	@Test
	public void testIsNPComplexity() {
		assertFalse(g1.isNPComplexity());
		assertFalse(g2.isNPComplexity());
	}

}
