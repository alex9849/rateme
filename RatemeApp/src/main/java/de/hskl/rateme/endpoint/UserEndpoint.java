package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.User;
import de.hskl.rateme.model.ValidationException;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.RatingService;
import de.hskl.rateme.service.UserService;
import de.hskl.rateme.util.EscherPlzValidator;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/user")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@RegisterProvider(IllegalAccessException.class)
@Singleton
public class UserEndpoint {
    @Inject
    private UserService userService;
    @Inject
    private AccessService accessService;
    @Inject
    private RatingService ratingService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@RequestBody(required = true) User user) {
        System.out.println("createUser");
        user.setId(0);
        user.setModifyDt(null);
        user.setCreateDt(null);
        Validator.validate(user);
        if(!EscherPlzValidator.validateCityAndPlz(user.getZip(), user.getCity())) {
            throw new ValidationException("PLZ passt nicht zur Stadt");
        }
        userService.createUser(user);
        return Response.ok().entity(user.cloneForFrontend()).build();
    }

    @GET
    @Path("{userId}/ratings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatingsByUser(@CookieParam("LoginID") String loginIdString, @PathParam("userId") long userId) throws IllegalAccessException {
        System.out.println("getRatingsByUser");
        if(!accessService.isLoggedIn(loginIdString)) {
            throw new IllegalAccessException("Not logged in!");
        }
        int loginUserId = accessService.getUserId(UUID.fromString(loginIdString));
        if(loginUserId != userId) {
            throw new IllegalAccessException("You are not allowed so fetch all ratings of another user!");
        }
        return Response.ok().entity(ratingService.getRatingsByUser(loginUserId)).build();
    }
}
