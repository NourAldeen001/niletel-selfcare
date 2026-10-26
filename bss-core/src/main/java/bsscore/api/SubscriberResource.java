package bsscore.api;

import bsscore.common.DateFormats;
import bsscore.common.ResponseHelper;
import bsscore.model.SubscriberProfile;
import bsscore.security.SessionUtil;
import bsscore.service.SubscriberService;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("/")
public class SubscriberResource {

    @EJB
    private SubscriberService subscriberService;

    @GET
    @Path("subscriber/me")
    public Response getMyProfile(@Context HttpServletRequest request) {
        String currentMsisdn = SessionUtil.currentMsisdn(request);
        SubscriberProfile subscriberProfile = subscriberService.getMyProfile(currentMsisdn);
        JsonObject data = Json.createObjectBuilder()
                .add("MSISDN", subscriberProfile.msisdn())
                .add("FULL_NAME", subscriberProfile.fullName())
                .add("STATUS", subscriberProfile.status())
                .add("TARIFF_CODE", subscriberProfile.tariffCode())
                .add("TARIFF_NAME", subscriberProfile.tariffName())
                .add("BALANCE_PIASTERS", subscriberProfile.balancePiasters())
                .add("ACTIVATION_DT", DateFormats.DATE.format(subscriberProfile.activationDate()))
                .build();
        return ResponseHelper.ok(data);
    }
}
