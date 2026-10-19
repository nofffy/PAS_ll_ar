package controllers;

import managers.UserManager;
import model.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import plantExeptions.UserLoginAlreadyUsedException;

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
        try {
            User user = userManager.getUser(id);
            return Response.ok(user).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/login/{login}")
    public Response getUserByExactLogin(@PathParam("login") String login) {
        User user = userManager.getUserByLogin(login);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @POST
    public Response createUser(User user) {
        try {
            userManager.createUser(user);
            return Response.status(Response.Status.CREATED).build(); // 201 Created
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (UserLoginAlreadyUsedException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/activate")
    public Response activateUser(@PathParam("id") Long id) {
        try {
            userManager.activateUser(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") Long id) {
        try {
            userManager.deactivateUser(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}