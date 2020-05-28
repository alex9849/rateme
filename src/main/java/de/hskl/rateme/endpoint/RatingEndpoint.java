package de.hskl.rateme.endpoint;

import de.hskl.rateme.model.Rating;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.RatingService;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/rating")
@Singleton
public class RatingEndpoint {
    @Inject
    AccessService accessService;
    @Inject
    RatingService ratingService;

    @GET
    @Path("/poi/{poiid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatingsByPoi(@PathParam("poiid") long poiId) {
        return Response.ok().entity(ratingService.getRatingsByPoi(poiId)).build();
    }

    @GET
    @Path("/user/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatingsByUser(@CookieParam("LoginID") String loginIdString, @PathParam("userid") int poiId) {
        if(loginIdString == null) {
            return Response.status(401).build();
        }
        UUID loginId = UUID.fromString(loginIdString);
        if(!accessService.isLoggedIn(loginId)) {
            return Response.status(401).build();
        }
        return Response.ok().entity(ratingService.getRatingsByUser(poiId)).build();
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rate(@CookieParam("LoginID") String loginIdString, @RequestBody Rating rating) {
        if(loginIdString == null) {
            return Response.status(401).build();
        }
        UUID loginId = UUID.fromString(loginIdString);
        if(!accessService.isLoggedIn(loginId)) {
            return Response.status(401).build();
        }
        int userId = accessService.getUserId(loginId);
        rating.setCreateDt(null);
        rating.setModifyDt(null);
        rating.setUserId(userId);
        Validator.validate(rating);
        ratingService.createRating(rating);
        return Response.ok().build();
    }
}
