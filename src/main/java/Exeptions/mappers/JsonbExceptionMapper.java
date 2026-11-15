package Exeptions.mappers;

import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonbExceptionMapper implements ExceptionMapper<JsonbException> {
    @Override
    public Response toResponse(JsonbException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format or enum value: " + e.getMessage()).build();
    }
}