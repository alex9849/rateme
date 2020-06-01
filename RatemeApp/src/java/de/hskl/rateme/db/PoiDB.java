package de.hskl.rateme.db;

import de.hskl.rateme.model.Poi;
import de.hskl.rateme.model.PoiTag;
import de.hskl.rateme.model.RatemeDbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PoiDB {

    private final String loadPoiSqlQuery = "SELECT poi.osm_id , ST_Y (poi.position) AS lat , ST_X(poi.position) AS lon, poi_type"
            + ", tag.tag, tag.value FROM rateme_poi poi JOIN rateme_poi_tag tag ON tag.osm_id = poi.osm_id ORDER BY poi.osm_id, tag.tag";
    private final String loadPoiByIdSqlQuery = "SELECT poi.osm_id , ST_Y (poi.position) AS lat , ST_X(poi.position) AS lon, poi_type"
            + ", tag.tag, tag.value FROM rateme_poi poi JOIN rateme_poi_tag tag ON tag.osm_id = poi.osm_id AND poi.osm_id = ? ORDER BY poi.osm_id, tag.tag";

    private DBConnection dbConnection = DBConnection.getInstance();

    public Collection<Poi> loadPois()
    {
        Map<Long, Poi> loadedPois = new HashMap<>();

        try (Connection connection = dbConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(loadPoiSqlQuery); ResultSet rs = pstmt.executeQuery())
        {
            while (rs.next())
            {
                Long osmId = rs.getLong(1);

                Poi poi = loadedPois.get(osmId);
                if (poi == null)
                {
                    poi = new Poi(rs.getLong(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4));
                    loadedPois.put(osmId, poi);
                }

                PoiTag poiTag = new PoiTag(osmId, rs.getString(5), rs.getString(6));
                poi.addTag(poiTag);
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new RatemeDbException("ERROR loadPois", ex);
        }

        return loadedPois.values();
    }

    public Poi loadPoi(long osmId)
    {
        try (Connection connection = dbConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(loadPoiByIdSqlQuery))
        {
            pstmt.setLong(1, osmId);

            try (ResultSet rs = pstmt.executeQuery())
            {
                Poi loadedPoi = null;

                while (rs.next())
                {
                    if (loadedPoi == null)
                    {
                        loadedPoi = new Poi(rs.getLong(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4));
                    }

                    PoiTag poiTag = new PoiTag(osmId, rs.getString(5), rs.getString(6));
                    loadedPoi.addTag(poiTag);
                }

                return loadedPoi;
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new RatemeDbException("ERROR loadPoiById", ex);
        }
    }
}
