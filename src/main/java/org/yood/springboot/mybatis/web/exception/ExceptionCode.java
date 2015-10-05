package org.yood.springboot.mybatis.web.exception;

public final class ExceptionCode {

    public static final class Validation {
        public static final String NAME_LENGTH_OUT_OF_RANGE = "_name_length_out_of_range";
        public static final String AGE_TOO_YOUNG = "_age_too_young";
        public static final String AGE_TOO_OLD = "_age_too_old";
        public static final String PASSWORD_LENGTH_OUT_OF_RANGE = "_password_length_out_of_range";
        public static final String PHONE_LENGTH_OUT_OF_RANGE = "_phone_length_out_of_range";
    }
}
