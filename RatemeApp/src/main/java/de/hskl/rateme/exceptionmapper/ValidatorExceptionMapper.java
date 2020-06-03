package de.hskl.rateme.exceptionmapper;

import de.hskl.rateme.model.exception.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Date;

@Provider
public class ValidatorExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException e) {
        System.out.println("ValidationException! Returning code 400. Message: " + e.getMessage());

        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(400);
        response.setTimestamp(new Date());
        response.setMessage(e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }
}
