package factory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RequestParserTest {
	private RequestParser parser;

	@Before
	public void setUp() throws Exception {
		parser=new RequestParser();
	}

	@Test(expected=SyntaxErrorException.class)
	public void testParseRequest1() throws SyntaxErrorException {
		
		assertTrue(parser.parseRequest("Abus(x;c,m) Vins(c,m;\"A\")").size()==2);
		assertTrue(parser.parseRequest("Abus(x;c,1986) Vins(c,m;\"A\")").size()==2);
	}
	
	@Test
    public void testParseRequest2() throws SyntaxErrorException{
		
		assertTrue(parser.parseRequest("Abus(x;c,m), Vins(c,m;\"A\")").size()==2);
		assertEquals(parser.parseRequest("Abus(x;c,m), Vins(c,m;\"A\")").get(0), "Abus(x;c,m)");
		assertEquals(parser.parseRequest("Abus(x;c,m), Vins(c,m;\"A\")").get(1), "Vins(c,m;\"A\")");
	}
	

	@Test(expected=BadRequestException.class)
	public void testGetConjonctiveRequest() throws RequestError {
		assertTrue(parser.getConjonctiveRequest("Abus(x;c,m) ,Vins(c,m;\"A\")", "y").getAtoms().size()==3);
	}

}
