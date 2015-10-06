package org.yood.rsa.web.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusinessException extends Exception {

    private BusinessException() {
    }

    private final List<ExceptionBody> exceptionBodies = new ArrayList<>();

    public static BusinessException fromException(ExceptionBody... exceptionBodies) {
        BusinessException exception = new BusinessException();
        Collections.addAll(exception.getExceptionBodies(), exceptionBodies);
        return exception;
    }

    public static BusinessException fromException(List<ExceptionBody> exceptionBodies) {
        return fromException(exceptionBodies.toArray(new ExceptionBody[0]));
    }

    public List<ExceptionBody> getExceptionBodies() {
        return exceptionBodies;
    }
}
