package ru.yandex.practicum.filmorate.stringvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxLengthStringValidator implements ConstraintValidator<MaxLengthString, String> {
    Integer validString;

    @Override
    public void initialize(MaxLengthString constraintAnnotation) {
        validString = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value.length() <= validString;
    }
}
