package com.kenzan;

import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
