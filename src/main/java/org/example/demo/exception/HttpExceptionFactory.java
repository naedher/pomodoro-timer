package org.example.demo.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class HttpExceptionFactory {

    private static final Map<Integer, Function<String, HttpException>> exceptionMap = initExceptionMap();

    private static Map<Integer, Function<String, HttpException>> initExceptionMap() {
        HashMap<Integer, Function<String, HttpException>> map = new HashMap<>();

        map.put(400, BadRequestException::new);
        map.put(401, UnauthorizedException::new);
        map.put(403, ForbiddenException::new);
        map.put(404, NotFoundException::new);
        map.put(500, InternalServerErrorException::new);

        return map;
    }

    public static HttpException getHttpException(int statusCode, String message) {
        if (!exceptionMap.containsKey(statusCode)) {
            throw new RuntimeException("Unexpected error");
        }
        return exceptionMap.get(statusCode).apply(message);
    }
}
