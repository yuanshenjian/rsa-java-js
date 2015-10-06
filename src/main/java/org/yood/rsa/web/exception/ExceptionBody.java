package org.yood.rsa.web.exception;


public class ExceptionBody {

    private final String field;
    private final String exceptionCode;

    private ExceptionBody(String field, String exceptionCode) {
        this.field = field;
        this.exceptionCode = exceptionCode;
    }

    public static ExceptionBody of(String field, String exceptionCode) {
        return new ExceptionBody(field, exceptionCode);
    }

    public String getField() {
        return field;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

}
