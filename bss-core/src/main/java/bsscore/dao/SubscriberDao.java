package bsscore.dao;

import bsscore.model.Subscriber;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SubscriberDao extends BaseDAO {

    public Optional<Subscriber> findByMsisdn(String msisdn) {
        return queryOne("SELECT msisdn, full_name, pin_hash, status " +
                "FROM subscriber WHERE msisdn = ?",
                rs -> new Subscriber(
                        rs.getString("msisdn"),
                        rs.getString("full_name"),
                        rs.getString("pin_hash"),
                        rs.getString("status")),
                msisdn);
    }
}
