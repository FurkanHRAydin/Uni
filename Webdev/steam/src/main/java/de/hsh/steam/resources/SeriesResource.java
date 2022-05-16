/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package de.hsh.steam.resources;

import de.hsh.steam.entities.Series;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.services.SteamService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * REST Web Service
 *
 * @author Strac
 */
@Path("series")
@RequestScoped
public class SeriesResource {
    @Inject
    SteamService steamService;
    @Inject
    SerializedSeriesRepository serializedSeriesRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSeries() {
        List<Series> series = serializedSeriesRepository.getAllSeries();
        if (series == null) {
            return Response.status(404).build();
        } else {
            return Response.ok().entity(series).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{substring}")
    public Response getSeries(@PathParam("substring")String substring){
        List<Series> subseries = serializedSeriesRepository.getAllSerieWithTitle(substring);
        if (subseries == null){
            return Response.status(404).build();
        } else {
            return Response.ok().entity(subseries).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSeries(Series series, @Context UriInfo uriInfo){        
         Series newseries = serializedSeriesRepository.addOrModifySeries(series);
         UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
         uriBuilder.path(series.getTitle());
         return Response.created(uriBuilder.build()).entity(newseries).build();
     }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{series}")
    public Response modifySeries(Series series, @Context UriInfo uriInfo){        
         Series newseries = serializedSeriesRepository.addOrModifySeries(series);
         UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
         uriBuilder.path(series.getTitle());
         return Response.created(uriBuilder.build()).entity(newseries).build();
     }
}
    

