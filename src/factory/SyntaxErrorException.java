package factory;

public class SyntaxErrorException extends RequestError {
    
	public SyntaxErrorException(String message) {
		super(message);
		errorType="Syntax error";
	}

	public SyntaxErrorException(String message,Throwable throwable){
        super(message,throwable);
        errorType="Syntax error";
    }

}
