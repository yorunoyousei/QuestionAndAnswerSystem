package com.echarm.qasystem.comment.error;

public class ServerSideProblemException extends RuntimeException implements AttachedErrorBody{
    public static final int SERVER_ERROR_CODE = 3001;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ServerSideProblemException(String msg) {
        super(msg);
    }

    @Override
    public void setErrorBody(ErrorBody body) {
        // nothing happens
    }

    @Override
    public ErrorBody getErrorBody() {
        return new ErrorBody(SERVER_ERROR_CODE, "Server Side Problem!", "Some Server Error Occurs!");
    }

}
