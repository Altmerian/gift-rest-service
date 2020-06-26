package com.epam.esm.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The annotated element must be either {@code null} or not empty string. */
@Documented
@Constraint(validatedBy = NullOrNotEmptyStringValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotEmptyConstraint {
  String message() default "A field cannot be empty";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
