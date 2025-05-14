package org.example.exceptions;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}

