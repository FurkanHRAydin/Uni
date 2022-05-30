/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package de.hsh.steam.resources;

import de.hsh.steam.entities.Rating;
import de.hsh.steam.entities.Score;
import de.hsh.steam.entities.Series;
import de.hsh.steam.entities.User;
import de.hsh.steam.services.SteamService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author lushaj
 */
@Path("users/{name}/ratings")
@XmlRootElement
public class RatingResource {

    SteamService steamService = SteamService.getInstance();

    @Path("/search")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllRatings(@PathParam("name") String username) {
        ArrayList<Series> allSeriesOfUser = steamService.getAllSeriesOfUser(username);
        ArrayList<Rating> allRatingsOfUser = new ArrayList<>();
        for (Series serie : allSeriesOfUser) {
            allRatingsOfUser.add(this.steamService.getRating(serie.getTitle(), username));
        }

        if (allSeriesOfUser == null) {
            return Response.status(404).build();
        } else {
            return Response.ok().entity(allRatingsOfUser).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSeries(Rating rating) {
        System.out.println("REST searchSeries arrived");
        if (rating == null) {
            return Response.status(406).entity(steamService.getAllSeries()).build();
        }

        // titel ist id => nur ein Treffer
        if (rating.getRatedSeries().getTitle() != null) {
            ArrayList<Series> arrSeries = steamService.getAllSeriesWithTitle(rating.getRatedSeries().getTitle());
            if (arrSeries.size() <= 0) {
                return Response.ok().entity("Keine Treffer").build();
            }

            Series series = arrSeries.get(0);
            Rating seriesRating = steamService.getRating(series.getTitle(),
                    rating.getRatingUser().getUsername());

            // Alle Kriterien müssen erfüllt werden
            if (rating.getRatedSeries().getGenre() != null
                    && series.getGenre() != rating.getRatedSeries().getGenre()
                    || rating.getRatedSeries().getNumberOfSeasons() > 0
                    && series.getNumberOfSeasons() != rating.getRatedSeries().getNumberOfSeasons()
                    || rating.getRatedSeries().getStreamedBy() != null
                    && series.getStreamedBy() != rating.getRatedSeries().getStreamedBy()
                    || rating.getScore() != null
                    && seriesRating.getScore() != rating.getScore()
                    || rating.getRemark() != null
                    && !seriesRating.getRemark().equals(rating.getRemark())) {
                return Response.ok().entity("Keine Treffer").build();
            }

            System.out.println(series.getTitle());
            return Response.ok().entity(seriesRating).build();
        }

        // Wenn kein Titel angegeben ist
        List<Series> searchSeries = steamService.search(
                rating.getRatingUser().getUsername(),
                rating.getRatedSeries().getGenre(),
                rating.getRatedSeries().getStreamedBy(),
                rating.getScore());

        ArrayList<Rating> ratedSeries = new ArrayList<>();
        for (Series series : searchSeries) {
            Rating seriesRating = steamService.getRating(series.getTitle(),
                    rating.getRatingUser().getUsername());

            // falls weitere Kriterien nicht erfüllt dann skippen
            if (rating.getRatedSeries().getNumberOfSeasons() > 0
                    && series.getNumberOfSeasons() != rating.getRatedSeries().getNumberOfSeasons()
                    || rating.getRemark() != null
                    && !seriesRating.getRemark().equals(rating.getRemark())) {
                continue;
            }

            ratedSeries.add(seriesRating);
        }

        if (ratedSeries.isEmpty()) {
            return Response.ok().entity("Keine Teffer").build();
        }
        return Response.ok().entity(ratedSeries).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrModifySeries(Rating rating) {
        System.out.println("REST addOrModifySeries arrived");
        if (rating == null || rating.getScore() == null
                || rating.getRemark().equals("")
                || rating.getRatedSeries().getTitle().equals("")
                || rating.getRatedSeries().getNumberOfSeasons() <= 0
                || rating.getRatedSeries().getGenre() == null
                || rating.getRatedSeries().getStreamedBy() == null) {
            return Response.status(406).entity("Bitte füllen Sie alle Felder aus!").build();
        }

        Score score = null;
        String remark = null;
        User ratingUser = new User();
        Series ratedSeries = new Series();

        Rating newRating = new Rating(score, remark, ratingUser, ratedSeries);
        newRating.getRatingUser().setUsername(rating.getRatingUser().getUsername());
        score = rating.getScore();
        remark = rating.getRemark();
        newRating.getRatedSeries().setTitle(rating.getRatedSeries().getTitle());
        newRating.getRatedSeries().setNumberOfSeasons(rating.getRatedSeries().getNumberOfSeasons());
        newRating.getRatedSeries().setGenre(rating.getRatedSeries().getGenre());
        newRating.getRatedSeries().setStreamedBy(rating.getRatedSeries().getStreamedBy());
        // user werden nicht doppelt hinzugeführt
        newRating.getRatedSeries().putOnWatchListOfUser(rating.getRatingUser());

        steamService.addOrModifySeries(newRating.getRatedSeries().getTitle(),
                newRating.getRatedSeries().getNumberOfSeasons(),
                newRating.getRatedSeries().getGenre(),
                newRating.getRatedSeries().getStreamedBy(),
                newRating.getRatingUser().getUsername(), score, remark);
        return Response.ok().entity("Serie erfolgreich hinzugefügt/bearbeitet").build();
    }
}
