package org.yood.rsa.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.yood.rsa.entity.User;
import org.yood.rsa.web.exception.ExceptionCode;

@Component
public class UserValidator extends GenericValidator<User> {

    @Override
    public Class<User> supportClass() {
        return User.class;
    }

    @Override
    public void validateTarget(User user, Errors errors) {
        if (StringUtils.isEmpty(user.getName()) || !user.getName().matches("[\\w]{3,55}")) {
            errors.rejectValue("name", ExceptionCode.Validation.NAME_LENGTH_OUT_OF_RANGE);
        }
        if (null != user.getPhone() && !user.getPhone().matches("\\d{7,15}")) {
            errors.rejectValue("phone", ExceptionCode.Validation.PHONE_LENGTH_OUT_OF_RANGE);
        }
        if (!user.getPassword().matches(".{3,255}")) {
            errors.rejectValue("password", ExceptionCode.Validation.PASSWORD_LENGTH_OUT_OF_RANGE);
        }
        if (user.getAge() < 3) {
            errors.rejectValue("age", ExceptionCode.Validation.AGE_TOO_YOUNG);
        }
        if (user.getAge() > 100) {
            errors.rejectValue("age", ExceptionCode.Validation.AGE_TOO_OLD);
        }
        if (null == user.getRoles() || user.getRoles().isEmpty()) {

        }
    }
}
