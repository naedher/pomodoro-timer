package org.example.exceptions;

public class ForbiddenException extends HttpException {
    public ForbiddenException(String message) {
        super(message);
    }
}
