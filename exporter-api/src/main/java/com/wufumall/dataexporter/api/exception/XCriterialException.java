package com.wufumall.dataexporter.api.exception;

public class XCriterialException extends RuntimeException {

    private static final long serialVersionUID = -5080193047417775631L;

    private String errorCode;

    private String errorMSG;

    public XCriterialException() {
        super();
    }

    public XCriterialException(String errorCode, String errorMSG) {
        super(errorMSG);
        this.errorCode = errorCode;
        this.errorMSG = errorMSG;
    }

    public XCriterialException(String errorCode, String errorMSG, Throwable throwable) {
        super(errorMSG, throwable);
        this.errorCode = errorCode;
        this.errorMSG = errorMSG;
    }

    public XCriterialException(Throwable throwable) {
        super(throwable);
    }

}
