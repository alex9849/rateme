package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.LoginData;
import de.hskl.rateme.model.User;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.UserService;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/session")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@RegisterProvider(IllegalAccessException.class)
@Singleton
public class SessionEndpoint {
    @Inject
    private UserService userService;
    @Inject
    private AccessService accessService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(@RequestBody(required = true) LoginData loginData) throws IllegalAccessException {
        System.out.println("loginUser");
        Validator.validate(loginData);
        UUID loginId = userService.loginUser(loginData);
        int userId = accessService.getUserId(loginId);
        User user = userService.loadUser(userId);
        NewCookie loginCookie = new NewCookie("LoginID", loginId.toString());
        return Response.ok().cookie(loginCookie).entity(user.cloneForFrontend()).build();
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@CookieParam("LoginID") String loginId) {
        System.out.println("logoutUser");
        if(loginId != null) {
            userService.logout(UUID.fromString(loginId));
        }
        return Response.ok().cookie((NewCookie) null).build();
    }
}