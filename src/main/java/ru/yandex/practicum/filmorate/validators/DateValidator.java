package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<Date, LocalDate> {
    LocalDate validDate;

    @Override
    public void initialize(Date constraintAnnotation) {
        validDate = LocalDate.parse(constraintAnnotation.afterThis(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return validDate.isBefore(localDate);
    }
}
