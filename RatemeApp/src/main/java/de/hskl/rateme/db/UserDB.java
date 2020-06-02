package de.hskl.rateme.db;

import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.User;

import java.sql.*;

public class UserDB {

    public int createUser(User user) {
        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement pstmt = con.prepareStatement("insert into rateme_user (username, E_Mail, firstname, lastname, street, streetNr, zip, city, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getFirstname());
            pstmt.setString(4, user.getLastname());
            pstmt.setString(5, user.getStreet());
            pstmt.setString(6, user.getStreetNr());
            pstmt.setString(7, user.getZip());
            pstmt.setString(8, user.getCity());
            pstmt.setString(9, user.getPassword());
            pstmt.execute();
            ResultSet genKeys = pstmt.getGeneratedKeys();
            while (genKeys.next()) {
                user.setId(genKeys.getInt(1));
                return genKeys.getInt(1);
            }
            throw new RatemeDbException("Could not insert into rateme_rating");
        } catch (SQLException e) {
            e.printStackTrace();
            if(e.getErrorCode() == 2601 || e.getErrorCode() == 2627) {
                throw new RatemeDbException("A user with this username already exists!", e);
            }
            throw new RatemeDbException("Error create user", e);
        }
    }

    public User loadUser(int userId) {
        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement pStmt = con.prepareStatement("select * from rateme_user where user_id = ?");
            pStmt.setInt(1, userId);
            pStmt.execute();
            ResultSet rs = pStmt.getResultSet();
            while (rs.next()) {
                return parseRs(rs);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RatemeDbException("Error load User", e);
        }
    }

    public User loadUser(String userName) {
        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement pStmt = con.prepareStatement("select * from rateme_user where username = ?");
            pStmt.setString(1, userName);
            pStmt.execute();
            ResultSet rs = pStmt.getResultSet();
            while (rs.next()) {
                return parseRs(rs);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RatemeDbException("Error load User", e);
        }
    }

    private User parseRs(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("E_Mail"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setStreet(rs.getString("street"));
        user.setStreetNr(rs.getString("streetNr"));
        user.setZip(rs.getString("zip"));
        user.setCity(rs.getString("city"));
        user.setPassword(rs.getString("password"));
        user.setCreateDt(rs.getDate("create_dt"));
        user.setModifyDt(rs.getDate("modify_dt"));
        return user;
    }

}
