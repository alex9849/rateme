package de.hskl.rateme.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private DataSource dataSource;
    private static DBConnection instance;

    private DBConnection() {
        Context ctxt;
        try {
            ctxt = new InitialContext();
            this.dataSource = (DataSource) ctxt.lookup("jdbc/mySQL");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if(instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    protected Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
