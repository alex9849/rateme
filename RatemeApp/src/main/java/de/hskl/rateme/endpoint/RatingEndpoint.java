package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.Rating;
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
    public Response rate(@CookieParam("LoginID") String loginIdString, @RequestBody Rating rating) throws IOException, IllegalAccessException {
        System.out.println("createRate");
        if(!accessService.isLoggedIn(loginIdString)) {
            throw new IllegalAccessException("Not logged in!");
        }
        int userId = accessService.getUserId(UUID.fromString(loginIdString));
        rating.setCreateDt(null);
        rating.setModifyDt(null);
        rating.setUserId(userId);
        Validator.validate(rating);
        ratingService.createRating(rating);
        return Response.ok().build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatings(@CookieParam("LoginID") String loginIdString, @QueryParam("user") Long userId, @QueryParam("poi") Long poiId) throws IllegalAccessException {
        System.out.println("getRatings");

        if((userId == null) == (poiId == null)) {
            throw new RatemeDbException("Getting ratings requires exactly one filter!");
        }
        Collection<Rating> ratings = null;
        if(userId != null)
            ratings = getRatingsByUser(loginIdString, userId);
        if(poiId != null)
            ratings = getRatingsByPoi(poiId);

        return Response.ok().entity(ratings).build();
    }

    private Collection<Rating> getRatingsByUser(String loginIdString, Long userId) throws IllegalAccessException {
        if(!accessService.isLoggedIn(loginIdString)) {
            throw new IllegalAccessException("Not logged in!");
        }
        int loginUserId = accessService.getUserId(UUID.fromString(loginIdString));
        if(loginUserId != userId) {
            throw new IllegalAccessException("You are not allowed so fetch all ratings of another user!");
        }
        return ratingService.getRatingsByUser(loginUserId);
    }

    private Collection<Rating> getRatingsByPoi(Long poiId) {
        return ratingService.getRatingsByPoi(poiId);
    }
}
