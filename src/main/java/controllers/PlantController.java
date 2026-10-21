package controllers;

import dtos.PlantDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import managers.PlantManager;
import model.Plant;

import java.util.List;

@Path("/plants")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlantController {

    @Inject
    private PlantManager plantManager;

    @GET
    public Response getPlants(@QueryParam("available") Boolean isAvailable) {
        List<Plant> plants;
        if (isAvailable != null) {
            if (isAvailable) {
                plants = plantManager.findAllAvailable();
            } else {
                plants = plantManager.findAllUnavailable();
            }
        } else {
            plants = plantManager.getPlants();
        }
        return Response.ok(plants).build();
    }

    @GET
    @Path("/{id}")
    public Response getPlantById(@PathParam("id") Long id) {
        try {
            Plant plant = plantManager.getPlant(id);
            return Response.ok(plant).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response createPlant(@Valid PlantDTO plant) {
        try {
            plantManager.createPlant(plant);
            return Response.status(Response.Status.CREATED).build(); // 201 Created
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlant(@PathParam("id") Long id) {
        try {
            plantManager.removePlant(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}
