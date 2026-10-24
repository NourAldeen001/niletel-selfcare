package bsscore.dao;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HealthDao extends BaseDAO {

    public boolean isDatabaseUp() {
        return queryOne("SELECT 1", rs -> rs.getInt(1)).isPresent();
    }
}
