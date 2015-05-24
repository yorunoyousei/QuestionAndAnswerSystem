package com.echarm.qasystem.question.error;

public class MissingParameterErrorBody extends ErrorBody{
    public static final int CODE = 2001;
    public static final String MESSAGE = "Missing Parameter";

    public MissingParameterErrorBody(String description) {
        super(CODE, MESSAGE, description);
    }

    public static String generateDescription(String parameterName, String parameterLocation) {
        return String.format("Parameter \"%s\" in %s is missing!", parameterName, parameterLocation);
    }
}
