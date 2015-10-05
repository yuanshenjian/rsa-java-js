package org.yood.springboot.mybatis.web.exception;

public class UnAuthorizedException extends Exception {

    private Object responseBody;

    private UnAuthorizedException() {
        responseBody = new Object();
    }

    public static UnAuthorizedException newInstance() {
        return new UnAuthorizedException();
    }

    public UnAuthorizedException responseBody(Object responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public Object getResponseBody() {
        return responseBody;
    }
}
