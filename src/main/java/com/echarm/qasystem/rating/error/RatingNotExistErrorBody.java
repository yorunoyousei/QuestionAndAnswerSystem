package com.echarm.qasystem.rating.error;

public class RatingNotExistErrorBody extends ErrorBody{
	public static final int CODE = 1004;
	public static final String MESSAGE = "Rating does not exist";

	public RatingNotExistErrorBody(String ratingId) {
		super(CODE, MESSAGE, generateDescription(ratingId));
	}

	public static String generateDescription(String ratingId) {
		return String.format("Rating with ID: \"%s\" does not exist!", ratingId);
	}
}
