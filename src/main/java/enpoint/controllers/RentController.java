package enpoint.controllers;

import enpoint.DTOs.CreateUpdateRentDTO;
import enpoint.DTOs.RentResponseDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import enpoint.managers.RentManager;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/rents")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RentController {

    @Inject
    RentManager rentManager;

    @Context
    private UriInfo uriInfo;

    @GET
    public Response getAllRents() {
        return Response.ok(rentManager.getRents()).build();
    }

    @GET
    @Path("/{id}")
    public Response getRentById(@PathParam("id") String id) {
        RentResponseDTO rent = rentManager.getRent(id);
        return Response.ok(rent).build(); // 200 OK
    }

    @POST
    public Response createRent(@Valid CreateUpdateRentDTO rent) {
        RentResponseDTO createdRent = rentManager.createRent(rent);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdRent.getId()).build();
        return Response.created(location).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRent(@PathParam("id") String id) {
        rentManager.removeRent(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/client/{id}/current")
    public Response getCurrentRentsByClientId(@PathParam("id") String id) {
        List<RentResponseDTO> rents = rentManager.getCurrentRentsByClientId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/client/{id}/past")
    public Response getPastRentsByClientId(@PathParam("id") String id) {
        List<RentResponseDTO> rents = rentManager.getPastRentsByClientId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/plant/{id}/current")
    public Response getCurrentRentsByPlantId(@PathParam("id") String id) {
        List<RentResponseDTO> rents = rentManager.getCurrentRentsByPlantId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/plant/{id}/past")
    public Response getPastRentsByPlantId(@PathParam("id") String id) {
        List<RentResponseDTO> rents = rentManager.getPastRentsByPlantId(id);
        return Response.ok(rents).build();
    }
}
