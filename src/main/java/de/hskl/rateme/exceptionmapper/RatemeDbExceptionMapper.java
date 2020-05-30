package de.hskl.rateme.exceptionmapper;

import de.hskl.rateme.model.RatemeDbException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Date;

@Provider
public class RatemeDbExceptionMapper implements ExceptionMapper<RatemeDbException> {
    @Override
    public Response toResponse(RatemeDbException e) {
        System.out.println("RatemeDbException! Returning code 500. Message: " + e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(500);
        response.setTimestamp(new Date());
        response.setMessage(e.getMessage());
        return Response.status(500).entity(response).build();
    }
}
