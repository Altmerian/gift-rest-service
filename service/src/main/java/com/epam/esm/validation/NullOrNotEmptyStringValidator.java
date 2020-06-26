package com.epam.esm.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotEmptyStringValidator implements ConstraintValidator<NullOrNotEmptyConstraint, String> {

  @Override
  public void initialize(NullOrNotEmptyConstraint constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.length() > 0;
  }
}
