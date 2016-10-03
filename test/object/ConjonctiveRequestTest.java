package object;

import static org.junit.Assert.*;



import org.junit.Before;
import org.junit.Test;

import factory.BadRequestException;
import factory.RequestParser;



public class ConjonctiveRequestTest {

	private Atom R,T;
	private ConjonctiveRequest q;
	@Before
	public void setUp() throws Exception {
		R=new Atom("Abus(x;c,m)","x");
		T=new Atom("Vins(c,m;\"A\")","x"); 		
		q=new RequestParser().getConjonctiveRequest("Abus(x;c,m),Vins(c,m;\"A\")", "x");
	}

	@Test
	public void testGetHead() {
		assertTrue(q.getHead().size()==1);
		assertTrue(q.getHead().get(0).equals("x"));
	}

	@Test
	public void testGetAtoms() {
		assertTrue(q.getAtoms().size()==2);
		assertTrue(q.getAtoms().get(0).isEquals(R));
		assertTrue(q.getAtoms().get(1).isEquals(T));
	}

	

	@Test
	public void testExternalDependencies() {
		try {
			assertTrue(q.externalDependencies(R).size()==0);
			assertTrue(q.externalDependencies(T).size()==2);
			assertTrue(q.externalDependencies(T).contains("c"));
			assertTrue(q.externalDependencies(T).contains("m"));
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDependencies() {
		try {
			assertTrue(q.dependencies(q.df(),R).size()==2);
			assertTrue(q.dependencies(q.df(),T).size()==2);
			assertTrue(q.dependencies(q.df(),R).contains("c"));
			assertTrue(q.dependencies(q.df(),R).contains("m"));
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAttackedAtoms() {
		try {
			assertNull(q.getIndexAttackedAtoms(q.getAtoms().get(1)));
			assertTrue(q.getIndexAttackedAtoms(q.getAtoms().get(0)).size()==1);
			assertTrue(q.getIndexAttackedAtoms(q.getAtoms().get(0)).get(0)==1);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
