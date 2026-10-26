package bsscore.service;

import bsscore.dao.SubscriberDao;
import bsscore.model.SubscriberProfile;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class SubscriberService {

    @Inject
    private SubscriberDao subscriberDao;

    public SubscriberProfile getMyProfile(String msisdn) {
        return subscriberDao.findProfileByMsisdn(msisdn)
                .orElseThrow(() -> new IllegalStateException("Logged-in subscriber has no row"));
    }
}
