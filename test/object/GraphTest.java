package object;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import factory.RequestParser;

public class GraphTest {
	private Graph g1,g2;

	@Before
	public void setUp() throws Exception {
		g1=new Graph(new RequestParser().getConjonctiveRequest("R(x; y), S(y;z), T(z;x), U(x;u), V(x,u; v)", ""), new Atom("S(y;z)", ""));
		g2=new Graph(new RequestParser().getConjonctiveRequest("R(x; y), S(y ;z), T(z;x), U(x;u), V(x,u; v)", ""), new Atom("V(x,u;v)", ""));
	}

	@Test
	public void testGetVertexes() {
		assertTrue(g1.getVertexes().size()==5 && g2.getVertexes().size()==5);
	}

	@Test
	public void testGetEdges() {
		assertTrue(g1.getEdges().size()==7);
		assertTrue(g2.getEdges().size()==0);
	}

}
