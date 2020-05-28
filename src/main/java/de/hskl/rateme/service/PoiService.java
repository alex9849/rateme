package de.hskl.rateme.service;

import de.hskl.rateme.db.PoiDB;
import de.hskl.rateme.db.PoiTagDB;
import de.hskl.rateme.model.Poi;
import de.hskl.rateme.model.PoiTag;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class PoiService {
    @Inject
    private PoiDB poiDB;
    @Inject
    private PoiTagDB poiTagDB;

    public Collection<Poi> getAllPoi() {
        return poiDB.loadPois();
    }

    public Collection<PoiTag> getPoiTags(long poiId) {
        return poiTagDB.loadTagsForPoi(poiId);
    }
}
