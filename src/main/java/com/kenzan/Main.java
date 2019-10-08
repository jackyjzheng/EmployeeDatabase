package com.kenzan;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/kenzan/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() throws IOException, ParseException {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.kenzan package
        final ResourceConfig rc = new ResourceConfig().packages("com.kenzan");
        rc.register(new LoggingFeature(Logger.getLogger(Main.class.getName()), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_TEXT, 8192));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter default username: ");
        String user = scanner.nextLine();
        System.out.println("Enter default password: ");
        String password = scanner.nextLine();

        EmployeeDatabase.getDatabase().setCredentials(user, password);
        EmployeeDatabase.getDatabase().readFromFile(System.getProperty("user.dir") + "/employees.csv");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParseException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

