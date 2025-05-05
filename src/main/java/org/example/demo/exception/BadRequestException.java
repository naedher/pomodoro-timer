package org.example.demo.exception;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message);
    }
}
