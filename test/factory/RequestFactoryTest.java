package factory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RequestFactoryTest {
	private RequestParser parser;
	private RequestFactory factory;

	@Before
	public void setUp() throws Exception {
		parser=new RequestParser();
		factory=new RequestFactory();
	}

	@Test(expected=BadRequestException.class)
	public void testMakeConjonctiveRequest() throws BadRequestException, SyntaxErrorException {
		assertTrue(factory.makeConjonctiveRequest(parser.parseRequest("Abus(x;x,m) ,Vins(c,m;\"A\")"),
				"i").getAtoms().size()==3);
		assertTrue(factory.makeConjonctiveRequest(parser.parseRequest("Abus(x;c,m),Abus(x;c,m) ,Vins(c,m;\"A\")"),
				"x").getAtoms().size()==3);
	}

}
