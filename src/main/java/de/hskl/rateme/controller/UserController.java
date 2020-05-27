package de.hskl.rateme.controller;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Singleton
public class UserController {

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser() {
        System.out.println("createUser");

        return Response.ok().build();
    }

}
