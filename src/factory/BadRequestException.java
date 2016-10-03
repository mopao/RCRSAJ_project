package factory;

public class BadRequestException extends RequestError {

	public BadRequestException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
		errorType="Bad request";
	}
	
	public BadRequestException(String message) {
		super(message);
		errorType="Bad request";
	}

}
