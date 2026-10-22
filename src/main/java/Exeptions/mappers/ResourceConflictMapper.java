package Exeptions.mappers;

import Exeptions.ResourceConflictException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceConflictMapper implements ExceptionMapper<ResourceConflictException> {
    @Override
    public Response toResponse(ResourceConflictException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
