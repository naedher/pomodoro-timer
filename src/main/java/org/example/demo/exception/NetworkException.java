package org.example.demo.exception;

public class NetworkException extends HttpException {
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
