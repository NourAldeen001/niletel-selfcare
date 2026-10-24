package bsscore.service;

import bsscore.common.BusinessException;
import bsscore.common.ErrorCode;
import bsscore.dao.SubscriberDao;
import bsscore.model.Subscriber;
import bsscore.security.HashUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Stateless
public class AuthService {

    @Inject
    private SubscriberDao subscriberDao;

    public Subscriber loginSubscriber(String msisdn, String pin) {
        String pinEnteredHash = HashUtil.sha256Hex(pin);

        Subscriber subscriber = subscriberDao.findByMsisdn(msisdn)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH001));

        if(!hashesMatch(pinEnteredHash, subscriber.pinHash())) {
            throw new BusinessException(ErrorCode.AUTH001);
        }

        if(!subscriber.isActive()) {
            throw new BusinessException(ErrorCode.AUTH002);
        }

        return subscriber;
    }

    private boolean hashesMatch(String pin1, String pin2) {
        return MessageDigest.isEqual(
                pin1.getBytes(StandardCharsets.UTF_8),
                pin2.getBytes(StandardCharsets.UTF_8)
        );
    }
}
