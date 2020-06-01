package de.hskl.rateme.exceptionmapper;

import de.hskl.rateme.model.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Date;

@Provider
public class ValidatorExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException e) {
        System.out.println("ValidationException! Returning code 422. Message: " + e.getMessage());
        e.printStackTrace();
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(422);
        response.setTimestamp(new Date());
        response.setMessage(e.getMessage());
        return Response.status(422).entity(response).build();
    }
}
