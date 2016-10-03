package object;
import static org.junit.Assert.*;

import object.Atom;
import object.DF;

import org.junit.Before;
import org.junit.Test;

import factory.BadRequestException;


public class DFTest {
	
	private Atom R,T;

	@Before
	public void setUp() throws Exception {
		R=new Atom("Vins(x;c,m)","x");
		T=new Atom("Abus(c,m;\"A\")","x"); 
	}

	@Test
	public void testGetKey() {
		try {
			assertTrue(R.df().getKey().isEmpty());
			assertTrue(T.df().getKey().equals("cm"));
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	@Test
	public void testGetDependency() {
		try {
			assertTrue(R.df().getDependency().equals("cm"));
			assertTrue(T.df().getDependency().isEmpty());
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testIsEquals() {
		try {
			assertTrue(R.df().isEquals(new DF(R)));
			assertFalse(R.df().isEquals(new DF(T)));
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
