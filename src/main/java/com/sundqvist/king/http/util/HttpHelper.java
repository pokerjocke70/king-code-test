package com.sundqvist.king.http.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpHelper {


    public static void sendResponse(HttpExchange exchange, String body, int status) {
        try (exchange) {
            exchange.sendResponseHeaders(status, body != null ? body.length() : 0);
            if (body != null) {
                exchange.getResponseBody().write(body.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFirstPathParameter(URI requestURI) {
        return getPathParameter(requestURI, 0);
    }

    public static String getPathParameter(URI requestURI, int position) {
        return requestURI.getPath().split("/")[position + 1]; // First position will be empty string
    }

    public static String getFirstQueryParameter(URI requestURI) {
        return requestURI.getQuery() != null ? requestURI.getQuery().split("=")[1] : null;
    }

    public static String readBody(HttpExchange httpExchange) {
        try {
            return new String(httpExchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
