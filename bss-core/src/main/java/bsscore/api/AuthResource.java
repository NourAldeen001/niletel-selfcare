package bsscore.api;

import bsscore.common.ResponseHelper;
import bsscore.common.Validation;
import bsscore.model.Subscriber;
import bsscore.security.Role;
import bsscore.security.SessionUtil;
import bsscore.service.AuthService;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {

    @EJB
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(@FormParam("msisdn") String msisdn,
                          @FormParam("pin") String pin,
                          @Context HttpServletRequest request) {
        Validation.requireMatch(msisdn, Validation.MSISDN);
        Validation.requireMatch(pin, Validation.PIN);

        Subscriber subscriber = authService.loginSubscriber(msisdn, pin);

        SessionUtil.login(request, subscriber.msisdn(), Role.SUB);

        JsonObject data = Json.createObjectBuilder()
                .add("MSISDN", subscriber.msisdn())
                .add("FULL_NAME", subscriber.fullName())
                .add("ROLE_CODE", Role.SUB.name())
                .build();

        return ResponseHelper.ok(data);
    }

    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        SessionUtil.logout(request);
        return ResponseHelper.ok();
    }
}
