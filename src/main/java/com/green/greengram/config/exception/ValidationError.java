package com.green.greengram.config.exception;

import lombok.*;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationError {
    private String field;
    private String message;

    public static ValidationError of(final FieldError fieldError) {
        return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }

    @Override
    public String toString() {
        return String.format("field: %s, message: %s", field, message);
    }
}