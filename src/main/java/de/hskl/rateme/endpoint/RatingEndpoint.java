package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.Rating;
import de.hskl.rateme.service.AccessService;
import de.hskl.rateme.service.RatingService;
import de.hskl.rateme.util.ImageUtils;
import de.hskl.rateme.util.Validator;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

@Path("/rating")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@RegisterProvider(IllegalAccessException.class)
@Singleton
public class RatingEndpoint {
    @Inject
    AccessService accessService;
    @Inject
    RatingService ratingService;

    @GET
    @Path("/poi/{poiid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatingsByPoi(@PathParam("poiid") long poiId) {
        System.out.println("getRatingsByPoi");
        return Response.ok().entity(ratingService.getRatingsByPoi(poiId)).build();
    }

    @GET
    @Path("/own")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatingsByUser(@CookieParam("LoginID") String loginIdString) throws IllegalAccessException {
        System.out.println("getRatingsByUser");
        if(!accessService.isLoggedIn(loginIdString)) {
            throw new IllegalAccessException("Not logged in!");
        }
        int userId = accessService.getUserId(UUID.fromString(loginIdString));
        return Response.ok().entity(ratingService.getRatingsByUser(userId)).build();
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rate(@CookieParam("LoginID") String loginIdString, @RequestBody Rating rating) throws IOException, IllegalAccessException {
        System.out.println("createRate");
        if(!accessService.isLoggedIn(loginIdString)) {
            throw new IllegalAccessException("Not logged in!");
        }
        int userId = accessService.getUserId(UUID.fromString(loginIdString));
        rating.setCreateDt(null);
        rating.setModifyDt(null);
        rating.setUserId(userId);
        Validator.validate(rating);
        if(rating.getImage() != null) {
            BufferedImage img = ImageUtils.readBase64Image(rating.getImage());
            double hight = 60;
            double witdh = img.getWidth() / (img.getHeight() / hight);
            img = ImageUtils.resizeImage(img, (int) witdh, (int) hight);
            rating.setImage(ImageUtils.toBase64Image(img));
        }
        ratingService.createRating(rating);
        return Response.ok().build();
    }
}
