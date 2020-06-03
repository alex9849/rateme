package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.User;
import de.hskl.rateme.model.exception.UnauthorizedException;
import de.hskl.rateme.model.exception.ValidationException;
import de.hskl.rateme.service.AccessService;
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
import javax.ws.rs.core.UriBuilder;
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
    AccessService accessService;

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
        return Response.created(UriBuilder.fromResource(this.getClass())
            .path(user.getId() + "").build()).entity(user.cloneForFrontend()).build();
    }

    @GET
    @Path("{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@CookieParam("LoginID") UUID loginId, @PathParam("userid") int userId) throws UnauthorizedException {
        System.out.println("getUser");
        User user = accessService.getUserIfLoggedIn(loginId);
        if(user == null)
            throw new UnauthorizedException("Not logged in!");
        if(user.getId() != userId)
            throw new UnauthorizedException("You are not allowed to get other users!");

        return Response.ok().entity(user.cloneForFrontend()).build();
    }
}
