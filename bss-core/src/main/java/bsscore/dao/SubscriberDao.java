package bsscore.dao;

import bsscore.model.Subscriber;
import bsscore.model.SubscriberProfile;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
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

    public Optional<SubscriberProfile> findProfileByMsisdn(String msisdn) {
        return queryOne("""
                        SELECT s.msisdn, s.full_name, s.status, s.tariff_code,
                               t.tariff_name, s.balance_piasters,
                               s.activation_dt
                        FROM subscriber s
                        JOIN tariff_plan t
                            ON s.tariff_code = t.tariff_code
                        WHERE msisdn = ?
                        """,
                rs -> new SubscriberProfile(
                        rs.getString("msisdn"),
                        rs.getString("full_name"),
                        rs.getString("status"),
                        rs.getString("tariff_code"),
                        rs.getString("tariff_name"),
                        rs.getLong("balance_piasters"),
                        rs.getObject("activation_dt", LocalDate.class)),
                msisdn);
    }
}
