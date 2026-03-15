package org.acme.ada.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof WebApplicationException webEx) {
            int status = webEx.getResponse().getStatus();
            String reason = Response.Status.fromStatusCode(status) != null
                    ? Response.Status.fromStatusCode(status).getReasonPhrase()
                    : "HTTP Error";

            ErrorResponse errorResponse = new ErrorResponse(
                    status,
                    reason,
                    webEx.getMessage(),
                    LocalDateTime.now()
            );

            return Response.status(status)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Internal Server Error",
                exception.getClass().getName() + ": " + exception.getMessage(),
                LocalDateTime.now()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}