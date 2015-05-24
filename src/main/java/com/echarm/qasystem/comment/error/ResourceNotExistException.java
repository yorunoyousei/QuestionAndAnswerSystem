package com.echarm.qasystem.comment.error;

public class ResourceNotExistException extends Exception implements AttachedErrorBody{

	/**
	 * This exception is thrown when some resource specified in the request doesn't exist.
	 * Resources include Category and Question
	 *
	 * Attachable ErrorBody subclasses:
	 * 		CategoryNotExistErrorBody
	 * 		QuestionNotExistErrorBody
	 *      CommentNotExistErrorBody
	 */
	private static final long serialVersionUID = 1L;

	private ErrorBody errorBody;

	public ResourceNotExistException(String msg) {
		super(msg);
	}

	@Override
	public void setErrorBody(ErrorBody body) {
		// only accept specific errors
		if (body instanceof CategoryNotExistErrorBody ||
		    body instanceof QuestionNotExistErrorBody  ||
			body instanceof CommentNotExistErrorBody) {
			this.errorBody = body;
		}
	}

	@Override
	public ErrorBody getErrorBody() {
		return errorBody;
	}

}
