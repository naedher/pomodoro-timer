package org.example.exceptions;

public class ConflictException extends HttpException {
    public ConflictException(String message) {
        super(message);
    }
}
