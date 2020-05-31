package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.PoiTag;
import de.hskl.rateme.service.PoiService;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/poitag")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@Singleton
public class PoiTagEndpoint {
    @Inject
    private PoiService poiService;

    @GET
    @Path("{poiid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoiTags(@PathParam("poiid") long poiId) {
        System.out.println("getAllPoiTags");
        Collection<PoiTag> allPoi = poiService.getPoiTags(poiId);
        return Response.ok().entity(allPoi).build();
    }
}
