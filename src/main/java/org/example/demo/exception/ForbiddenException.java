package org.example.demo.exception;

public class ForbiddenException extends HttpException {
    public ForbiddenException(String message) {
        super(message);
    }
}
