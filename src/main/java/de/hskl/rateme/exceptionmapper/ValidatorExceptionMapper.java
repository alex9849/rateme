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
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(422);
        response.setTimestamp(new Date());
        response.setMessage(e.getMessage());
        return Response.status(422).entity(response).build();
    }

    public static class ExceptionResponse {
        private String message;
        private Date timestamp;
        private int status;

        private ExceptionResponse() {}

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
