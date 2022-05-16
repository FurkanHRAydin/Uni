/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package de.hsh.steam.resources;

import de.hsh.steam.entities.Rating;
import de.hsh.steam.entities.Series;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.services.SteamService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author SAbde
 */
@Path("users/{name}/ratings")
@RequestScoped
public class RatingResource {
    @Inject
    SteamService steamService;
    @Inject
    SerializedSeriesRepository serializedSeriesRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRatings(@PathParam("name") String username) {
        ArrayList<Series> allSeriesOfUser = steamService.getAllSeriesOfUser(username);
        ArrayList<Rating> allRatingsOfUser = new ArrayList<>();
        for(Series serie : allSeriesOfUser){
            allRatingsOfUser.add(this.steamService.getRating(serie.getTitle(), username));
        }

        if (allSeriesOfUser == null) {
            return Response.status(404).build();
        } else {
            return Response.ok().entity(allRatingsOfUser).build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{seriesname}")
    public Response getRating(@PathParam("name")String username, @PathParam("seriesname") String seriesname){
        Rating rating = this.steamService.getRating(seriesname, username);
        if (rating == null){
            return Response.status(404).build();
        } else {
            return Response.ok().entity(rating).build();
        }
    }
}
