package com.markets.demo.shared.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class QueryValidator implements ConstraintValidator<ValidQuery, String> {

    @Override
    public void initialize(final ValidQuery arg0) {

    }

    @Override
    public boolean isValid(final String query, final ConstraintValidatorContext context) {

        if (query == null || query.matches("^[^</=>`;:\"%!#&*$]*$")) {
            return true;
        }

        return false;
    }

}
