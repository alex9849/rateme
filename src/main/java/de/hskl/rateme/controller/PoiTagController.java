package de.hskl.rateme.controller;

import de.hskl.rateme.db.PoiTagDB;
import de.hskl.rateme.model.PoiTag;

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
@Singleton
public class PoiTagController {
    @Inject
    private PoiTagDB poiTagDB;

    @GET
    @Path("{poiid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoiTags(@PathParam("poiid") long poiId) {
        System.out.println("getAllPoiTags");
        Collection<PoiTag> allPoi = poiTagDB.loadTagsForPoi(poiId);
        return Response.ok().entity(allPoi).build();
    }
}
