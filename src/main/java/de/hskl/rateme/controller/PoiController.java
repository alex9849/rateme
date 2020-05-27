package de.hskl.rateme.controller;

import de.hskl.rateme.db.PoiDB;
import de.hskl.rateme.model.Poi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/poi")
@Singleton
public class PoiController {
    @Inject
    private PoiDB poiDB;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPoi() {
        System.out.println("getAllPoi");
        Collection<Poi> allPoi = poiDB.loadPois();
        return Response.ok().entity(allPoi).build();
    }
}
