package org.example.exceptions;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message);
    }
}
