package com.azienda.Exceptions;

import java.io.Serial;

public class IdNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1458488340100671889L;
    public IdNotFoundException(String message) {
        super(message);
    }
}
