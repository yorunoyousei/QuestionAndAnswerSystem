package com.echarm.qasystem.question.error;

public class EmptyParameterErrorBody extends ErrorBody{
    public static final int CODE = 2002;
    public static final String MESSAGE = "Empty Parameter";

    public EmptyParameterErrorBody(String description) {
        super(CODE, MESSAGE, description);
    }

    public static String generateDescription(String parameterName, String parameterLocation) {
        return String.format("Parameter \"%s\" in %s should not be empty!", parameterName, parameterLocation);
    }
}
