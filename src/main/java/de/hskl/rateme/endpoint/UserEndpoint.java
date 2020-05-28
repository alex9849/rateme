package de.hskl.rateme.endpoint;

import com.google.gson.Gson;
import de.hskl.rateme.model.LoginData;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.User;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.UserService;
import de.hskl.rateme.util.Validator;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/user")
@RegisterProvider(ValidatorExceptionMapper.class)
@Singleton
public class UserEndpoint {
    @Inject
    UserService userService;
    @Inject
    AccessService accessService;

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@RequestBody(required = true) User user) {
        try {
            System.out.println("createUser");
            user.setId(0);
            user.setModifyDt(null);
            user.setCreateDt(null);
            Validator.validate(user);
            userService.createUser(user);
            return Response.ok().entity(user).build();
        } catch (RatemeDbException e) {
            return Response.serverError().entity(new Gson().toJson(e.getMessage())).build();
        }
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(@RequestBody(required = true) LoginData loginData) {
        try {
            Validator.validate(loginData);
            UUID loginId = userService.loginUser(loginData);
            int userId = accessService.getUserId(loginId);
            User user = userService.loadUser(userId);
            NewCookie loginCookie = new NewCookie("LoginID", loginId.toString());
            return Response.ok().cookie(loginCookie).entity(user).build();
        } catch (RatemeDbException e) {
            return Response.serverError().entity(new Gson().toJson(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@CookieParam("LoginID") String loginId) {
        try {
            userService.logout(UUID.fromString(loginId));
            return Response.ok().cookie((NewCookie) null).build();
        } catch (RatemeDbException e) {
            return Response.serverError().entity(new Gson().toJson(e.getMessage())).build();
        }
    }
}
