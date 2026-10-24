package bsscore.dao;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseDAO {

    @Inject
    private Jdbc jdbc;

    protected <T> List<T> queryList(String sql,
                                    RowMapper<T> mapper,
                                    Object... params) {
        try(Connection conn = jdbc.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            bind(stmt, params);
            try(ResultSet rs = stmt.executeQuery()) {
                List<T> rows = new ArrayList<>();
                while(rs.next()) {
                    rows.add(mapper.map(rs));
                }
                return rows;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Query failed: " + sql, ex);
        }
    }

    protected <T> Optional<T> queryOne(String sql,
                                       RowMapper<T> mapper,
                                       Object... params) {
        List<T> rows = queryList(sql, mapper, params);
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    protected int update(String sql, Object... params) {
        try(Connection conn = jdbc.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            bind(stmt, params);
            return stmt.executeUpdate();
        }
        catch(SQLException ex) {
            throw new DataAccessException("Update failed: " + sql, ex);
        }

    }

    private static void bind(PreparedStatement stmt, Object[] params) throws SQLException {
        for(int i = 0; i < params.length; i++) {
            stmt.setObject(i+1, params[i]);
        }
    }

}
