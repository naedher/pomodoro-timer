package org.example.exceptions;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message);
    }
}