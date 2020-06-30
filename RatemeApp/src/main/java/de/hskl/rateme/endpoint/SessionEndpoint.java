package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.UnauthorizedExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.LoginData;
import de.hskl.rateme.model.User;
import de.hskl.rateme.model.exception.UnauthorizedException;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.UserService;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/session")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@RegisterProvider(UnauthorizedExceptionMapper.class)
@Singleton
public class SessionEndpoint {
    @Inject
    private UserService userService;
    @Inject
    private AccessService accessService;


    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(@RequestBody(required = true) LoginData loginData) throws UnauthorizedException {
        System.out.println("loginUser");
        Validator.validate(loginData);
        UUID loginId = accessService.login(loginData);
        int userId = accessService.getUserIdIfLoggedIn(loginId);
        User user = userService.loadUser(userId);
        NewCookie loginCookie = new NewCookie(new Cookie("LoginID", loginId.toString()), "RateMe-Login-Cookie", NewCookie.DEFAULT_MAX_AGE, true);
        return Response.ok().cookie(loginCookie).entity(user.cloneForFrontend()).build();
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@CookieParam("LoginID") String loginId) {
        System.out.println("logoutUser");
        if(loginId != null) {
            accessService.logout(UUID.fromString(loginId));
        }
        return Response.noContent().cookie((NewCookie) null).build();
    }
}
