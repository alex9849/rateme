package de.hskl.rateme.controller;

import de.hskl.rateme.db.UserDB;
import de.hskl.rateme.model.LoginData;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.RegistrationData;
import de.hskl.rateme.model.User;
import de.hskl.rateme.util.Password;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Path("/user")
@Singleton
public class UserController {
    @Inject
    UserDB userDB;

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@RequestBody(required = true) User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("createUser");
        user.setId(0);
        user.setModifyDt(null);
        user.setCreateDt(null);
        Validator.validate(user);
        if(userDB.loadUser(user.getUsername()) != null) {
            throw new RatemeDbException("A user with this username already exists!");
        }
        user.setPassword(Password.getSaltedHash(user.getPassword()));
        userDB.createUser(user);
        return Response.ok().build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(@RequestBody(required = true) LoginData loginData) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Validator.validate(loginData);
        User user = userDB.loadUser(loginData.getUsername());
        if(user == null) {
            throw new RatemeDbException("User does not exist!");
        }
        if(!Password.checkPassword(loginData.getPassword(), user.getPassword())) {
            throw new RatemeDbException("Password invalid!");
        }
        return Response.ok().build();
    }

}
