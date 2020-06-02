package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.Rating;
import de.hskl.rateme.model.User;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.RatingService;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Path("/rating")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@RegisterProvider(IllegalAccessException.class)
@Singleton
public class RatingEndpoint {
    @Inject
    private AccessService accessService;
    @Inject
    private RatingService ratingService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rate(@CookieParam("LoginID") UUID loginId, @RequestBody Rating rating) throws IOException, IllegalAccessException {
        System.out.println("createRate");
        User loggedInUser = accessService.getUserIfLoggedIn(loginId);
        if(loggedInUser == null)
            throw new IllegalAccessException("Not logged in!");
        rating.setCreateDt(null);
        rating.setModifyDt(null);
        rating.setUserId(loggedInUser.getId());
        rating.setUsername(loggedInUser.getUsername());
        Validator.validate(rating);
        ratingService.createRating(rating);
        return Response.created(UriBuilder.fromResource(this.getClass())
                .path(rating.getId() + "").build()).entity(rating).build();
    }

    @GET
    @Path("{ratingid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRating(@PathParam("ratingid") int ratingid) {
        return Response.ok().entity(ratingService.getRating(ratingid)).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatings(@CookieParam("LoginID") UUID loginId, @QueryParam("user") Long userId, @QueryParam("poi") Long poiId) throws IllegalAccessException {
        System.out.println("getRatings");
        if((userId == null) == (poiId == null)) {
            throw new RatemeDbException("Getting ratings requires exactly one filter!");
        }
        Collection<Rating> ratings = null;
        if(userId != null)
            ratings = getRatingsByUser(loginId, userId);
        if(poiId != null)
            ratings = getRatingsByPoi(poiId);

        return Response.ok().entity(ratings).build();
    }

    private Collection<Rating> getRatingsByUser(UUID loginId, Long userId) throws IllegalAccessException {
        System.out.println("getRatingsByUser");
        User loggedInUser = accessService.getUserIfLoggedIn(loginId);
        if(loggedInUser == null)
            throw new IllegalAccessException("Not logged in!");
        if(loggedInUser.getId() != userId) {
            throw new IllegalAccessException("You are not allowed so fetch all ratings of another user!");
        }
        return ratingService.getRatingsByUser(loggedInUser.getId());
    }

    private Collection<Rating> getRatingsByPoi(Long poiId) {
        System.out.println("getRatingsByPoi");
        return ratingService.getRatingsByPoi(poiId);
    }
}
