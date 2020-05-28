package de.hskl.rateme.endpoint;

import de.hskl.rateme.db.UserDB;
import de.hskl.rateme.model.LoginData;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.User;
import de.hskl.rateme.service.UserService;
import de.hskl.rateme.util.Password;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Path("/user")
@Singleton
public class UserEndpoint {
    @Inject
    UserDB userDB;
    @Inject
    UserService userService;

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@RequestBody(required = true) User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("createUser");
        user.setId(0);
        user.setModifyDt(null);
        user.setCreateDt(null);
        Validator.validate(user);
        userService.createUser(user);
        return Response.ok().build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(@RequestBody(required = true) LoginData loginData) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Validator.validate(loginData);
        UUID loginId = userService.loginUser(loginData);
        NewCookie loginCookie = new NewCookie("LoginID", loginId.toString());
        return Response.ok().cookie(loginCookie).build();
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@CookieParam("LoginID") String loginId) {
        userService.logout(UUID.fromString(loginId));
        return Response.ok().cookie((NewCookie) null).build();
    }

}
