/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package de.hsh.steam.resources;

import de.hsh.steam.entities.Genre;
import de.hsh.steam.entities.Series;
import de.hsh.steam.entities.Streamingprovider;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.services.SteamService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * REST Web Service
 *
 * @author lushaj
 */
@Path("series")
@XmlRootElement
public class SeriesResource {

    SteamService steamService = SteamService.getInstance();
    SerializedSeriesRepository serializedSeriesRepository = SerializedSeriesRepository.getInstance();

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllSeries() {
        List<Series> series = steamService.getAllSeries();
        if (series == null) {
            return Response.status(404).build();
        } else {
            return Response.ok().entity(series).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{substring}")
    public Response getSeries(@PathParam("substring") String substring) {
        List<Series> subseries = steamService.getAllSeriesWithTitle(substring);
        if (subseries == null) {
            return Response.status(404).build();
        } else {
            return Response.ok().entity(subseries).build();
        }
    }

    @GET
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSeries(@QueryParam("genre") String genre,
            @QueryParam("streamingprovider") String streamingprovider) {
        List<Series> searchSeries = steamService.search(null, Utils.stringToGenre(genre),
                Utils.stringToProvider(streamingprovider), null);
        return Response.ok().entity(searchSeries).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createSeries(Series series, @Context UriInfo uriInfo) {

        if (series == null) {
            return Response.status(415).build();
        } else {
            Series tempseries = this.serializedSeriesRepository.getSeriesObjectFromName(series.getTitle());

            if (tempseries == null) {
                String title = series.getTitle();
                int numberOfSeasons = series.getNumberOfSeasons();
                Genre genre = series.getGenre();
                Streamingprovider streamedBy = series.getStreamedBy();

                steamService.addOrModifySeries(title, numberOfSeasons, genre, streamedBy, null, null, null);

                series = this.serializedSeriesRepository.getSeriesObjectFromName(series.getTitle());
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                uriBuilder.path(series.getTitle());

                return Response.created(uriBuilder.build()).entity(series).build();
            } else {
                return Response.status(409).build();
            }

        }

    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response modifySeries(Series series, @Context UriInfo uriInfo) {
        if (series == null) {
            return Response.status(415).build();
        } else {
            String title = series.getTitle();
            int numberOfSeasons = series.getNumberOfSeasons();
            Genre genre = series.getGenre();
            Streamingprovider streamedBy = series.getStreamedBy();

            steamService.addOrModifySeries(title, numberOfSeasons, genre, streamedBy, null, null, null);

            series = this.serializedSeriesRepository.getSeriesObjectFromName(series.getTitle());

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(series.getTitle());
            return Response.created(uriBuilder.build()).entity(series).build();
        }
    }

}
