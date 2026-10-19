package controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import managers.RentManager;
import model.Rent;

import java.util.List;

@Path("/rents")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RentController {

    @Inject
    RentManager rentManager;

    @GET
    public Response getAllRents() {
        return Response.ok(rentManager.getRents()).build();
    }

    @GET
    @Path("/{id}")
    public Response getRentById(@PathParam("id") Long id) {
        try {
            Rent rent = rentManager.getRent(id);
            return Response.ok(rent).build(); // 200 OK
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response createRent(Rent rent) {
        try {
            rentManager.createRent(rent);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRent(@PathParam("id") Long id) {
        try {
            rentManager.removeRent(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/client/{id}/current")
    public Response getCurrentRentsByClientId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getCurrentRentsByClientId(id);
        if (rents.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(rents).build();
    }

    @GET
    @Path("/client/{id}/past")
    public Response getPastRentsByClientId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getPastRentsByClientId(id);
        if (rents.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(rents).build();
    }

    @GET
    @Path("/plant/{id}/current")
    public Response getCurrentRentsByPlantId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getCurrentRentsByPlantId(id);
        if (rents.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(rents).build();
    }

    @GET
    @Path("/plant/{id}/past")
    public Response getPastRentsByPlantId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getPastRentsByPlantId(id);
        if (rents.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(rents).build();
    }
}
