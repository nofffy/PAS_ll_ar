package Exeptions.mappers;

import Exeptions.UserLoginAlreadyUsedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserLoginAlreadyUsedMapper implements ExceptionMapper<UserLoginAlreadyUsedException> {
    @Override
    public Response toResponse(UserLoginAlreadyUsedException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
