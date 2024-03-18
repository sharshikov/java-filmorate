package ru.yandex.practicum.filmorate.datevalidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsBeforeValidator.class)
@Documented
public @interface IsBefore {
    String value();

    String message() default "Date is before";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
