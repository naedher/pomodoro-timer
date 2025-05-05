package org.example.demo.exception;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
