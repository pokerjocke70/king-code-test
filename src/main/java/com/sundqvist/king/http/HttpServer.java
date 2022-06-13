package com.sundqvist.king.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;

import static com.sundqvist.king.http.util.HttpHelper.*;

/**
 * HttpServer using com.sun.net.httpserver.HttpServer
 *
 * Starts a server at a user defined port.
 *
 */
public class HttpServer {

    private static final System.Logger LOGGER = System.getLogger(HttpServer.class.getName());

    private final AuthenticationFilter authenticationFilter;

    private final Set<Handler> handlers;

    private final int port;

    public HttpServer(AuthenticationFilter authenticationFilter, Set<Handler> handlers, int port) {
        this.authenticationFilter = authenticationFilter;
        this.handlers = handlers;
        this.port = port;
    }

    /**
     * Starts the http server on the root context
     *
     * @throws IOException
     */
    public void run() throws IOException {
        var httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 20);
        httpServer.createContext("/", this::handle).getFilters().add(authenticationFilter);
        LOGGER.log(System.Logger.Level.INFO, "Starting server on port {0}, Ctrl-C to abort", String.valueOf(port));
        httpServer.start();
    }

    private void handle(HttpExchange httpExchange) {

        handlers.stream()
                .filter(h -> h.supports(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod()))
                .findFirst()
                .map(h -> h.invoke(readBody(httpExchange), getFirstPathParameter(httpExchange.getRequestURI()), (String) httpExchange.getAttribute(AuthenticationFilter.USER_SESSION_ID)))
                .or(() -> Optional.of(new Response(404, "Not found")))
                .ifPresent(r -> sendResponse(httpExchange, r.body(), r.status()));
    }
}
