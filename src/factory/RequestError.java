package factory;

public abstract  class RequestError  extends Exception {

	protected String errorType="";
 
    public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	/**
     *nouvelle erreur de chargement
     * @param message l'explication de l'erreur
     */
    public RequestError(String message){
        super(message);
    }

    public RequestError(String message,Throwable throwable){
        super(message,throwable);
    }
    
}