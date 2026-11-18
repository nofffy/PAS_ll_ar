package api.controllers;

import api.DTOs.CreateUpdateUserDTO;
import api.DTOs.UserResponseDTO;
import jakarta.validation.Valid;
import managers.UserManager;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserManager userManager;

    @Context
    private UriInfo uriInfo;

    @GET
    public Response getUsers(@QueryParam("loginPattern") String loginPattern, @QueryParam("active") Boolean isActive) {
        List<UserResponseDTO> users;
        if (loginPattern != null && !loginPattern.isBlank()) {
            users = userManager.getUsersByLogin(loginPattern);
        } else if (isActive != null) {
            if (isActive) {
                users = userManager.findAllActiveUsers();
            } else {
                users = userManager.findAllInactiveUsers();
            }
        } else {
            users = userManager.getAllUsers();
        }
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") String id) {
        UserResponseDTO user = userManager.getUser(id);
        return Response.ok(user).build();
    }

    @GET
    @Path("/login/{login}")
    public Response getUserByExactLogin(@PathParam("login") String login) {
        UserResponseDTO user = userManager.getUserByLogin(login);
        return Response.ok(user).build();
    }

    @POST
    public Response createUser(@Valid CreateUpdateUserDTO user) {
        UserResponseDTO createdUser = userManager.createUser(user);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdUser.getId()).build();
        return Response.created(location).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") String id, @Valid CreateUpdateUserDTO userDTO) {
        userManager.updateUser(id, userDTO);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/activate")
    public Response activateUser(@PathParam("id") String id) {
        userManager.activateUser(id);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") String id) {
        userManager.deactivateUser(id);
        return Response.ok().build();
    }
}