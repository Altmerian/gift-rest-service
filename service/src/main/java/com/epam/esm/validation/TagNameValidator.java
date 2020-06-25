package com.epam.esm.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TagNameValidator implements ConstraintValidator<TagNameConstraint, String> {

  @Override
  public void initialize(TagNameConstraint constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.length() > 0;
  }
}
