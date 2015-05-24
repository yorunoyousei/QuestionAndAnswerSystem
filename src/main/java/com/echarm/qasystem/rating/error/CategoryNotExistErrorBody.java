package com.echarm.qasystem.rating.error;

public class CategoryNotExistErrorBody extends ErrorBody{
	public static final int CODE = 1001;
	public static final String MESSAGE = "Category does not exist";
	public CategoryNotExistErrorBody(String category) {
		super(CODE, MESSAGE, generateDescription(category));
	}
	
	public static String generateDescription(String category) {
		return String.format("Input category \"%s\" does not exist!", category);
	}
}
