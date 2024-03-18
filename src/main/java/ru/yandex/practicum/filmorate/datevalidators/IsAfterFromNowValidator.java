package ru.yandex.practicum.filmorate.datevalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class IsAfterFromNowValidator implements ConstraintValidator<IsAfterThanNow, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return LocalDate.now().isAfter(localDate);
    }
}
