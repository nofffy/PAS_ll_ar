package controllers;

import DTOs.UserDTO;
import jakarta.validation.Valid;
import managers.UserManager;
import model.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserManager userManager;

    @GET
    public Response getUsers(@QueryParam("loginPattern") String loginPattern, @QueryParam("active") Boolean isActive) {
        List<User> users;
        if (loginPattern != null && !loginPattern.isBlank()) {
            users = userManager.getUsersByLogin(loginPattern);
        } else if (isActive != null) {
            if (isActive) {
                users = userManager.findAllActiveUsers();
            } else {
                users = userManager.findAllUnactiveUsers();
            }
        } else {
            users = userManager.getAllUsers();
        }
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        User user = userManager.getUser(id);
        return Response.ok(user).build();
    }

    @GET
    @Path("/login/{login}")
    public Response getUserByExactLogin(@PathParam("login") String login) {
        User user = userManager.getUserByLogin(login);
        return Response.ok(user).build();
    }

    @POST
    public Response createUser(@Valid UserDTO user) {
        userManager.createUser(user);
        return Response.status(Response.Status.CREATED).build(); // 201 Created
    }

    @POST
    @Path("/{id}/activate")
    public Response activateUser(@PathParam("id") Long id) {
        userManager.activateUser(id);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") Long id) {
        userManager.deactivateUser(id);
        return Response.ok().build();
    }
}