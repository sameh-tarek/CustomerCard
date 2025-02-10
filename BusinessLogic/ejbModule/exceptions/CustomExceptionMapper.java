package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String errorMessage = "Internal server error";

        if (exception instanceof CustomerCardException) {
            CustomerCardException customEx = (CustomerCardException) exception;
            errorMessage = customEx.getMessage();
        } else if (exception instanceof CardNotFoundException) {
            CardNotFoundException notFoundEx = (CardNotFoundException) exception;
            statusCode = Response.Status.NOT_FOUND.getStatusCode();
            errorMessage = notFoundEx.getMessage();
        }
        
        exception.printStackTrace();
        return Response.status(statusCode).entity(new ErrorResponse(errorMessage, statusCode)).build();
    }
}