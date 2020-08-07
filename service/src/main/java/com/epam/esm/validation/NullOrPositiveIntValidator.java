package com.epam.esm.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrPositiveIntValidator implements ConstraintValidator<NullOrPositiveConstraint, Integer> {

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    return value == 0 || value > 0;
  }
}
