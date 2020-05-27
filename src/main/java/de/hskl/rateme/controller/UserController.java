package de.hskl.rateme.controller;

import de.hskl.rateme.db.UserDB;
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
        user.setPassword(Password.getSaltedHash(user.getPassword()));
        userDB.createUser(user);
        return Response.ok().build();
    }

}
