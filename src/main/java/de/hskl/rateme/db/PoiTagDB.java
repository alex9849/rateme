package de.hskl.rateme.db;


import de.hskl.rateme.model.Poi;
import de.hskl.rateme.model.PoiTag;
import de.hskl.rateme.model.RatemeDbException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PoiTagDB {

    public Collection<PoiTag> loadTagsForPoi(long osmId) {
        HashSet<PoiTag> poiTags = new HashSet<>();
        try (Connection con = DBConnection.getInstance().getConnection()) {
            PreparedStatement pStmt = con.prepareStatement("select * from rateme_poi_tag where osm_id = ?");
            pStmt.setLong(1, osmId);
            pStmt.execute();
            ResultSet rs = pStmt.getResultSet();
            while (rs.next()) {
                poiTags.add(new PoiTag(rs.getLong("osm_id"), rs.getString("tag"), rs.getString("value")));
            }
            return poiTags;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RatemeDbException("Error load PoiTags", e);
        }
    }
}
