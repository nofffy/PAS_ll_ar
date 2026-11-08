package enpoint.controllers;

import enpoint.DTOs.CreateUpdatePlantDTO;
import enpoint.DTOs.PlantResponseDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import enpoint.managers.PlantManager;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/plants")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlantController {

    @Inject
    private PlantManager plantManager;

    @Context
    private UriInfo uriInfo;

    @GET
    public Response getPlants(@QueryParam("available") Boolean isAvailable) {
        List<PlantResponseDTO> plants;
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
    public Response getPlantById(@PathParam("id") String id) {
        PlantResponseDTO plant = plantManager.getPlant(id);
        return Response.ok(plant).build();
    }

    @POST
    public Response createPlant(@Valid CreateUpdatePlantDTO plant) {
        PlantResponseDTO createdPlant = plantManager.createPlant(plant);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdPlant.getId()).build();
        return Response.created(location).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePlant(@PathParam("id") String id, @Valid CreateUpdatePlantDTO plantDTO) {
        plantManager.updatePlant(id, plantDTO);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlant(@PathParam("id") String id) {
        plantManager.removePlant(id);
        return Response.noContent().build();
    }
}
