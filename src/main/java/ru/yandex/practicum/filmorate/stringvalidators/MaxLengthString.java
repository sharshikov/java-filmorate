package ru.yandex.practicum.filmorate.stringvalidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxLengthStringValidator.class)
@Documented
public @interface MaxLengthString {
    int value();

    String message() default "Length string is bigger than limit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
