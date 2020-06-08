package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.Rating;
import de.hskl.rateme.model.User;
import de.hskl.rateme.model.exception.RatemeDbException;
import de.hskl.rateme.model.exception.UnauthorizedException;
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
    public Response rate(@CookieParam("LoginID") UUID loginId, @RequestBody Rating rating) throws IOException, UnauthorizedException {
        System.out.println("createRate");
        User loggedInUser = accessService.getUserIfLoggedIn(loginId);
        if(loggedInUser == null)
            throw new UnauthorizedException("Not logged in!");
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
    public Response getRatings(@CookieParam("LoginID") UUID loginId, @QueryParam("user") Integer userId, @QueryParam("poi") Long poiId) {
        System.out.println("getRatings");
        if((userId == null) == (poiId == null)) {
            throw new RatemeDbException("Getting ratings requires exactly one filter!");
        }
        Collection<Rating> ratings = null;
        if(userId != null) {
            System.out.println("getRatingsByUser");
            ratings = ratingService.getRatingsByUser(userId);
        }
        if(poiId != null) {
            System.out.println("getRatingsByPoi");
            ratings = ratingService.getRatingsByPoi(poiId);
        }
        return Response.ok().entity(ratings).build();
    }
}
