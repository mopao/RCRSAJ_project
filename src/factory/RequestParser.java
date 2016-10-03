package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.AttackGraph;
import object.ConjonctiveRequest;
import object.OrientedEdge;
import object.EdgeKind;

public class RequestParser {
	
    public RequestParser(){
    	
    }
    
    public ArrayList<String> parseRequest(String request) throws SyntaxErrorException{
    	
    	String REQUEST_REGEX = "((((((\\w)+)\\((((\\w)|(\"([^\"]*)\")),)*((\\w)|(\"([^\"]*)\"));(((\\w)|(\"([^\"]*)\")),)*((\\w)|(\"([^\"]*)\"))?\\),)))*(((((\\w)+)\\((((\\w)|(\"([^\"]*)\")),)*((\\w)|(\"([^\"]*)\"));(((\\w)|(\"([^\"]*)\")),)*((\\w)|(\"([^\"]*)\"))?\\)))))";
    	String ATOM_REGEX ="((((\\w)+)\\((((\\w)|(\"([^\"]*)\")),)*((\\w)|(\"([^\"]*)\"));(((\\w)|(\"([^\"]*)\")),)*((\\w)|(\"([^\"]*)\"))?\\)))";
    	
		//vérification syntaxe du corps de la requête
    	ArrayList<String> listAtoms= new ArrayList<String>();
    	String rewriteRequest=removeSpaces(request.trim());
    	rewriteRequest=rewriteRequest.replaceAll("(\\r|\\n)", "");
    	Pattern p = Pattern.compile(REQUEST_REGEX);
    	Matcher m = p.matcher(rewriteRequest);
        if(m.matches()){
        	p = Pattern.compile(ATOM_REGEX);
            m = p.matcher(rewriteRequest);
            while(m.find()) {                
                int start=m.start();
                int end=m.end();      
                listAtoms.add(rewriteRequest.substring(start, end));
             }
        	
        }
        else{
        	throw new SyntaxErrorException("Verify syntax of the body " +
        			"request.");
        }
        
      
    	if(listAtoms.size()>0) return listAtoms;
    	
    	
    	
    	return null;
    	
    }
    
    public ConjonctiveRequest getConjonctiveRequest(String request, String ans) throws RequestError{
    	RequestFactory factory= new RequestFactory();
    	String ANSWER_REGEX ="(((([a-z]),)*([a-z])))";
    	ArrayList<String> elts;
    	ans=ans.trim().replace(" ", "");
    	
    	//vérification syntaxe de la tête de la requête
    	Pattern p = Pattern.compile(ANSWER_REGEX);    	
    	Matcher m = p.matcher(ans);    	
    	if(!ans.isEmpty() &&!m.matches()){
    		throw new SyntaxErrorException("Verify syntax of the head " +
        			"request.");
    	}
		elts = parseRequest(request);
		ans=ans.trim().replace(",", "");
		if(elts!=null){
			
    		return factory.makeConjonctiveRequest(elts, ans);
		}	
	    
    	return null;
    	
    	
    }
    
    
    
    private String removeSpaces(String request){
    	String requestRewrote="";
    	String [] tab1=request.split("\"");
    	for (int i = 0; i < tab1.length; i++) {
    		if(i%2==0)
    			requestRewrote+=tab1[i].trim().replace(" ", "");
    		else
    			requestRewrote+="\""+tab1[i]+"\"";
		}
    	
    	return requestRewrote;
    }
    
    
    
}
