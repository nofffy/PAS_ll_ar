package controllers;

import logic.managers.UserManager;
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
    public Response getUsers(@QueryParam("loginPattern") String loginPattern) {
        List<User> users;
        if (loginPattern != null && !loginPattern.isBlank()) {
            users = userManager.getUsersByLogin(loginPattern);
        } else {
            users = userManager.getAllUsers();
        }
        return Response.ok(users).build(); // Zwraca 200 OK
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        User user = userManager.getUser(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }
        return Response.ok(user).build(); // 200 OK
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
        userManager.createUser(user);
        return Response.status(Response.Status.CREATED).build(); // 201 Created
    }

    @POST
    @Path("/{id}/activate")
    public Response activateUser(@PathParam("id") Long id) {
        User user = userManager.getUser(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userManager.activateUser(id);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") Long id) {
        User user = userManager.getUser(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userManager.deactivateUser(id);
        return Response.ok().build();
    }
}