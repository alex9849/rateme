package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.User;
import de.hskl.rateme.model.ValidationException;
import de.hskl.rateme.service.UserService;
import de.hskl.rateme.util.EscherPlzValidator;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@RegisterProvider(IllegalAccessException.class)
@Singleton
public class UserEndpoint {
    @Inject
    private UserService userService;

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
}
