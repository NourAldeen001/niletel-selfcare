package bsscore.dao;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
public class Jdbc {

    @Resource(lookup = "java:jboss/datasources/BssCoreDS")
    private DataSource dataSource;

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot get a database connection", ex);
        }
    }
}
