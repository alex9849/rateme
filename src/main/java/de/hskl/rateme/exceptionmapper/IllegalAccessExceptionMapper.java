package de.hskl.rateme.exceptionmapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Date;

@Provider
public class IllegalAccessExceptionMapper implements ExceptionMapper<IllegalAccessException> {
    @Override
    public Response toResponse(IllegalAccessException e) {
        System.out.println("IllegalAccessException! Returning code 401. Message: " + e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(401);
        response.setTimestamp(new Date());
        response.setMessage(e.getMessage());
        return Response.status(401).entity(response).build();
    }
}
