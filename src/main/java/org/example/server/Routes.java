package org.example.server;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Routes {
    public static final String LOGIN_PAGE = "/";
    public static final String LOGIN_ACTION = "/login";
    public static void configure(WebServer webServer) {
        webServer.routes(() -> {
        });
    }
}
