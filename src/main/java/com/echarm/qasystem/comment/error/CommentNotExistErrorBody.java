package com.echarm.qasystem.comment.error;

public class CommentNotExistErrorBody extends ErrorBody{
	public static final int CODE = 1003;
	public static final String MESSAGE = "Comment does not exist";

	public CommentNotExistErrorBody(String commentId) {
		super(CODE, MESSAGE, generateDescription(commentId));
	}

	public static String generateDescription(String commentId) {
		return String.format("Comment with ID: \"%s\" does not exist!", commentId);
	}
}
