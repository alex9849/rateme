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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Path("/rating")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
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
        return Response.ok().entity(ratingService.getRatingsByPoi(poiId)).build();
    }

    @GET
    @Path("/own")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatingsByUser(@CookieParam("LoginID") String loginIdString) {
        if(loginIdString == null) {
            return Response.status(401).build();
        }
        UUID loginId = UUID.fromString(loginIdString);
        if(!accessService.isLoggedIn(loginId)) {
            return Response.status(401).build();
        }
        return Response.ok().entity(ratingService.getRatingsByUser(accessService.getUserId(loginId))).build();
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rate(@CookieParam("LoginID") String loginIdString, @RequestBody Rating rating) throws IOException {
        if(loginIdString == null) {
            return Response.status(401).build();
        }
        UUID loginId = UUID.fromString(loginIdString);
        if(!accessService.isLoggedIn(loginId)) {
            return Response.status(401).build();
        }
        int userId = accessService.getUserId(loginId);
        rating.setCreateDt(null);
        rating.setModifyDt(null);
        rating.setUserId(userId);
        Validator.validate(rating);
        if(rating.getImage() != null) {
            BufferedImage img = ImageUtils.readBase64Image(rating.getImage());
            img = ImageUtils.resizeImage(img, img.getWidth() / 2, img.getHeight() / 2);
            File out = new File("image.png");
            ImageIO.write(img, "png", out);
        }
        ratingService.createRating(rating);
        return Response.ok().build();
    }
}
