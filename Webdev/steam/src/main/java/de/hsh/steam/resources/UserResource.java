/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package de.hsh.steam.resources;

import de.hsh.steam.entities.User;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.services.SteamService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * REST Web Service
 *
 * @author SAbde
 */
@Path("users")
@RequestScoped
public class UserResource {
    @Inject
    SteamService steamService;
    @Inject
    SerializedSeriesRepository serializedSeriesRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response gettAllUsers() {
        List<User> users = serializedSeriesRepository.getAllUsers();
        if (users == null) {
            return Response.status(404).build();
        } else {
            return Response.ok().entity(users).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public Response getUser(@PathParam("name")String name){
        User user = this.serializedSeriesRepository.getUserObject(name);
        if (user == null){
            return Response.status(404).build();
        } else {
            return Response.ok().entity(user).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user, @Context UriInfo uriInfo){        
        if( steamService.newUser(user.getUsername(), user.getPassword())){
            return Response.status(409).build();
        } else {
            this.serializedSeriesRepository.registerUser(user);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(user.getUsername());
            user = this.serializedSeriesRepository.getUserObject(user.getUsername());
            return Response.created(uriBuilder.build()).entity(user).build();
        }        
    }

//    @Path("/{name}/ratings")
//    public RatingResource getRating(){
//        return new RatingResource();
//    }
}
