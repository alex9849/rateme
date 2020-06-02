package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.Rating;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.RatingService;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
}
