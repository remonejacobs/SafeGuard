package org.example.server;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.NullSessionDataStore;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionHandler;
import org.example.model.Person;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;


public class WebServer {
    public static final String SESSION_USER_KEY = "user";
    private static final String PAGES_DIR = "/html";
    private static final String TEMPLATES_DIR = "/templates/";

    private final Javalin appServer;

    public WebServer() {
        // configure the thymeleaf template engine
        JavalinThymeleaf.configure(templateEngine());

        //create the app server configurations
        appServer = Javalin.create( config -> {
            // Add static file handling (e.g., CSS, JS) from /html directory.
            config.addStaticFiles(PAGES_DIR, Location.CLASSPATH);
            //  Add an access manager to control user access/security.
            config.accessManager(accessManager());
            // Set up session handling (cookies/sessions management).
            config.sessionHandler(sessionHandler());
        });

        Routes.configure(this);


    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start(5050);
    }

    @Nullable
    public static Person getPersonLoggedIn(Context context) {
        return context.sessionAttribute(SESSION_USER_KEY);
    }

    private static Supplier<SessionHandler> sessionHandler() {
        SessionHandler sessionHandler = new SessionHandler();
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(new NullSessionDataStore());
        sessionHandler.setSessionCache(sessionCache);
        sessionHandler.setHttpOnly(true);
        return () -> sessionHandler;
    }

    // The Access Manager controls access to your app's routes (endpoints).
    // It decides whether a request is allowed to proceed or should be blocked/redirected based on user roles,
    // authentication, or other logic.
    private AccessManager accessManager() {
        return new AccessManager() {
            @Override
            public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<RouteRole> set) throws Exception {
                if (hasNoSession(context)) {
                    context.redirect(Routes.LOGIN_PAGE);
                } else {
                    handler.handle(context);
                }
            }

            private boolean hasNoSession(@NotNull Context context) {
                Person loggedInPerson = context.sessionAttribute(SESSION_USER_KEY);
                return Objects.isNull(loggedInPerson) && !context.path().equals(Routes.LOGIN_ACTION);
            }
        };
    }



    public void routes(EndpointGroup group) {
        appServer.routes(group);
    }

    public void start(int port) {
        this.appServer.start(port);
    }

    public void stop() {
        this.appServer.stop();
    }

    public int port() {
        return appServer.port();
    }

    private TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix(TEMPLATES_DIR);
        templateEngine.setTemplateResolver(resolver);
        templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }
}