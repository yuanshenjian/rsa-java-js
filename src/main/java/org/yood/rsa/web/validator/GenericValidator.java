package org.yood.rsa.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class GenericValidator<T> implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return supportClass().equals(clazz);
    }

    public abstract Class<T> supportClass();

    @Override
    public void validate(Object target, Errors errors) {
        validateTarget((T) target, errors);
    }

    public abstract void validateTarget(T target, Errors errors);
}
