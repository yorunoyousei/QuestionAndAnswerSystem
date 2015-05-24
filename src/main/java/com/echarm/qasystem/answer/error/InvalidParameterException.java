package com.echarm.qasystem.answer.error;

public class InvalidParameterException extends Exception implements AttachedErrorBody{

	/**
	 * This exception is thrown when some parameter in the request is malformed.
	 * Parameters include Json body, header parameters and query parameters.
	 * 
	 * Attached ErrorBody subclasses:
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidParameterException(String msg) {
		super(msg);
	}
	
	private ErrorBody errorBody;

	@Override
	public void setErrorBody(ErrorBody body) {
		this.errorBody = body;		
	}

	@Override
	public ErrorBody getErrorBody() {
		return errorBody;
	}

}
