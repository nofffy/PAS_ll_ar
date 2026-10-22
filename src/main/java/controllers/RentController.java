package controllers;

import DTOs.RentDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
        Rent rent = rentManager.getRent(id);
        return Response.ok(rent).build(); // 200 OK
    }

    @POST
    public Response createRent(@Valid RentDTO rent) {
        rentManager.createRent(rent);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRent(@PathParam("id") Long id) {
        rentManager.removeRent(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/client/{id}/current")
    public Response getCurrentRentsByClientId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getCurrentRentsByClientId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/client/{id}/past")
    public Response getPastRentsByClientId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getPastRentsByClientId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/plant/{id}/current")
    public Response getCurrentRentsByPlantId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getCurrentRentsByPlantId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/plant/{id}/past")
    public Response getPastRentsByPlantId(@PathParam("id") Long id) {
        List<Rent> rents = rentManager.getPastRentsByPlantId(id);
        return Response.ok(rents).build();
    }
}
