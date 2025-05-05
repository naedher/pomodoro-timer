package org.example.demo.exception;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message);
    }
}
