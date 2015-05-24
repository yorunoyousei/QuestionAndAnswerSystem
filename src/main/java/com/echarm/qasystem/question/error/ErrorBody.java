package com.echarm.qasystem.question.error;

public class ErrorBody {
	private int code;
	private String message;
	private String description;

	public ErrorBody(int code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

	/*
	 * getter
	 */
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDescription() {
		return description;
	}

}
