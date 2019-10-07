package com.kenzan;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EmployeeResourceTest {
    private static HttpServer server;
    private static WebTarget target;
    private static EmployeeDatabase testDatabase;
    private static Employee testEmployee;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeClass
    public static void setUp() throws Exception {
        server = Main.startServer();

        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
        testDatabase = EmployeeDatabase.getDatabase();
        testDatabase.setCredentials("Jacky", "Zheng");

        Date testDateOfBirth = format.parse("1997-02-10");
        Date testEmploymentDate = format.parse("2019-10-06");
        testEmployee = new Employee(1, "Jacky", "Zheng", testDateOfBirth, testEmploymentDate);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.stop();
    }

    @After
    public static void verifyState() throws Exception {
        assertEquals(0, testDatabase.numberOfEmployees());
    }

    @Test
    public void testAddEmployee() {
        Response addEmployee = target.path("/employees").request()
                .post(Entity.json(testEmployee));
        Response alreadyAdded = target.path("/employees").request()
                .post(Entity.json(testEmployee));

        assertEquals(addEmployee.getStatus(), Response.Status.CREATED.getStatusCode());
        assertEquals(addEmployee.getStatus(), Response.Status.CONFLICT.getStatusCode());

        testDatabase.deleteEmployee(testEmployee.getID());
    }

    @Test
    public void testGetEmployee() {
        Response addEmployee = target.path("/employees").request()
                .post(Entity.json(testEmployee));

        Response getEmployeeResponse = target.path("/employees/1").request()
                .accept(MediaType.APPLICATION_JSON).get();
        Employee getEmployee = getEmployeeResponse.readEntity(Employee.class);

        assertEquals(getEmployeeResponse.getStatus(), Response.Status.OK.getStatusCode());
        assertEquals(getEmployee, testEmployee);

        Response getNonexistingEmployee = target.path("/employees/1").request()
                .accept(MediaType.APPLICATION_JSON).get();
        assertEquals(getNonexistingEmployee.getStatus(), Response.Status.NOT_FOUND.getStatusCode());

        testDatabase.deleteEmployee(testEmployee.getID());
    }

    @Test
    public void testDeleteEmployee() {
        Response addEmployee = target.path("/employees").request()
                .post(Entity.json(testEmployee));

        Response deleteEmployee = target.path("/employees/1").request()
                .delete();
        assertEquals(deleteEmployee.getStatus(), Response.Status.OK.getStatusCode());

        Response deleteAgain = target.path("/employees/1").request()
                .delete();
        assertEquals(deleteAgain.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }
}
