package com.echarm.qasystem.question.error;

public class NoContentException extends Exception implements AttachedErrorBody{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setErrorBody(ErrorBody body) {
		//nothing happened
	}

	@Override
	public ErrorBody getErrorBody() {
		System.out.println("NO_CONTENT");
		return null;
	}

}
