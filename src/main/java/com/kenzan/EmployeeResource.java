package com.kenzan;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.NoSuchAlgorithmException;

@Path("/employees")
public class EmployeeResource {
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response findAll() {
        return Response.status(Response.Status.OK.getStatusCode())
                .entity(EmployeeDatabase.getDatabase().getAllEmployees())
                .build();
    }

    /**
     *
     * @param id, Path parameter id of employee
     * @return Response with either NOT_FOUND or OK,
     *          If okay, return employee in either JSON or XML
     */
    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response findById(@PathParam("id") int id) {
        Employee targetEmployee = EmployeeDatabase.getDatabase().getEmployee(id);
        if (targetEmployee == null) {
           return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } else {
            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(targetEmployee)
                    .build();
        }
    }

    /**
     *
     * @param employee POST with employee object in request body in JSON Format
     * exampleBody = {
     *     "id": "4",
     *     "firstName": "Jacky",
     *     "middleInitial": "J",
     *     "lastName": "Zheng",
     *     "dateOfBirth": "1997-02-10",
     *     "dateOfEmployment": "2019-10-10"
     * }
     * Middle initial is optional
     * @return Response CREATED or CONFLICT if there is already Employee with same id in database
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEmployee(
            Employee employee, @Context UriInfo uriInfo) {
        Boolean noConflict = EmployeeDatabase.getDatabase().addEmployee(employee);
        int status = Response.Status.CREATED.getStatusCode();
        if (!noConflict) { status = Response.Status.CONFLICT.getStatusCode(); }
        return Response.status(status)
                .header(
                        "Location",
                        String.format("%Syss/%s",uriInfo.getAbsolutePath().toString(),
                                employee.getID())).build();
    }

    /**
     *
     * @param id Path parameter id, will either create or replace Employee at that ID
     * @param employee Same as with POST
     * @return Response CREATED if there wasn't already employee with that ID, else OK
     */

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmployee(@PathParam("id") int id, Employee employee) {
        Boolean didntAlreadyExist = EmployeeDatabase.getDatabase().putEmployee(id, employee);
        if (didntAlreadyExist) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.OK).build();
        }
    }

    /**
     *
     * @param header Include three authentication properties in header: username, time, and md5(username+password+time)
     * @param id ID of employee that is to be deleted
     * @return Response UNAUTHORIZED if incorrect credentials, OK if deleted, NOT_FOUND if not in database.
     */
    @DELETE
    @Path("{id}")
    public Response deleteEmployee(@Context HttpHeaders header, @PathParam("id") int id) throws NoSuchAlgorithmException {
        String username = header.getRequestHeader("username").get(0);
        Long time = Long.parseLong(header.getRequestHeader("time").get(0));
        String hash = header.getRequestHeader("hash").get(0);

        if (!EmployeeDatabase.getDatabase().authenticate(username, time, hash)) {
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
        }

        if (EmployeeDatabase.getDatabase().deleteEmployee(id)) {
           return Response.status(Response.Status.OK.getStatusCode()).build();
       } else {
           return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
       }
    }
}
