package com.evoluum.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvoluumException extends RuntimeException {

    private String errorMessage;

    public EvoluumException(String message) {
        super();
        this.errorMessage = message;
    }
}
