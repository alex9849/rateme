package de.hskl.rateme.endpoint;

import de.hskl.rateme.exceptionmapper.RatemeDbExceptionMapper;
import de.hskl.rateme.exceptionmapper.ValidatorExceptionMapper;
import de.hskl.rateme.model.Poi;
import de.hskl.rateme.service.PoiService;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/poi")
@RegisterProvider(ValidatorExceptionMapper.class)
@RegisterProvider(RatemeDbExceptionMapper.class)
@Singleton
public class PoiEndpoint {
    @Inject
    private PoiService poiService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPoi() {
        System.out.println("getAllPoi");
        Collection<Poi> allPoi = poiService.getAllPoi();
        return Response.ok().entity(allPoi).build();
    }
}
