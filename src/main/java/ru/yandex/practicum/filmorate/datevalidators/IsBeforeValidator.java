package ru.yandex.practicum.filmorate.datevalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IsBeforeValidator implements ConstraintValidator<IsBefore, LocalDate> {
    LocalDate validDate;

    @Override
    public void initialize(IsBefore constraintAnnotation) {
        validDate = LocalDate.parse(constraintAnnotation.value(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return validDate.isBefore(localDate);
    }
}
