package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        String errorMessage = "Internal server error";
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        if (exception instanceof CustomerCardException) {
            errorMessage = exception.getMessage();
        }
        
        exception.printStackTrace();

        return Response.status(statusCode)
                       .entity(new ErrorResponse(errorMessage, statusCode))
                       .build();
    }
}
