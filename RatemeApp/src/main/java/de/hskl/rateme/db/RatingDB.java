package de.hskl.rateme.db;


import de.hskl.rateme.model.Rating;
import de.hskl.rateme.model.exception.RatemeDbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RatingDB {

    public int createRating(Rating rating) {
        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement pstmt = con.prepareStatement("insert into rateme_rating (user_id, osm_id, grade, txt, image) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, rating.getUserId());
            pstmt.setLong(2, rating.getOsmId());
            pstmt.setInt(3, rating.getGrade());
            pstmt.setString(4, rating.getText());
            pstmt.setString(5, rating.getImage());
            pstmt.execute();
            ResultSet genKeys = pstmt.getGeneratedKeys();
            while (genKeys.next()) {
                rating.setId(genKeys.getInt(1));
                return genKeys.getInt(1);
            }
            throw new RatemeDbException("Could not insert into rateme_rating");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RatemeDbException("Error create Rating", e);
        }
    }

    public Rating loadRating(int ratingId) {
        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement pStmt = con.prepareStatement("select r.*, u.username as username from rateme_rating r join rateme_user u on r.user_id = u.user_id where r.rating_id = ?");
            pStmt.setInt(1, ratingId);
            pStmt.execute();
            ResultSet rs = pStmt.getResultSet();
            while (rs.next()) {
                return parseRs(rs);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RatemeDbException("Error load Ratings", e);
        }
    }

    public Collection<Rating> loadRatingsForPoi(long osmId) {
        List<Rating> ratings = new ArrayList<>();
        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement pStmt = con.prepareStatement("select r.*, u.username as username from rateme_rating r join rateme_user u on r.user_id = u.user_id where r.osm_id = ?");
            pStmt.setLong(1, osmId);
            pStmt.execute();
            ResultSet rs = pStmt.getResultSet();
            while (rs.next()) {
                ratings.add(parseRs(rs));
            }
            return ratings;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RatemeDbException("Error load Ratings", e);
        }
    }

    public Collection<Rating> loadRatingsForUser(int userId) {
        List<Rating> ratings = new ArrayList<>();
        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement pStmt = con.prepareStatement("select r.*, u.username as username from rateme_rating r join rateme_user u on r.user_id = u.user_id where r.user_id = ?");
            pStmt.setInt(1, userId);
            pStmt.execute();
            ResultSet rs = pStmt.getResultSet();
            while (rs.next()) {
                ratings.add(parseRs(rs));
            }
            return ratings;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RatemeDbException("Error load Ratings for User", e);
        }
    }

    private static Rating parseRs(ResultSet rs) throws SQLException {
        Rating rating = new Rating();
        rating.setId(rs.getInt("rating_id"));
        rating.setUserId(rs.getInt("user_id"));
        rating.setOsmId(rs.getLong("osm_id"));
        rating.setGrade(rs.getInt("grade"));
        rating.setText(rs.getString("txt"));
        rating.setImage(rs.getString("image"));
        rating.setCreateDt(rs.getTimestamp("create_dt").toLocalDateTime());
        rating.setModifyDt(rs.getTimestamp("modify_dt").toLocalDateTime());
        rating.setUsername(rs.getString("username"));
        return rating;
    }

}
