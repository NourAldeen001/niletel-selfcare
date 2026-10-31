package bsscore.api;


import bsscore.common.ResponseHelper;
import bsscore.model.DebitResult;
import bsscore.security.SessionUtil;
import bsscore.service.DebitService;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("/")
public class DebitResource {

    @EJB
    private DebitService debitService;
    @Context
    private HttpServletRequest request;

    @POST
    @Path("/subscriber/me/debit")
    public Response debit(@FormParam("amountPiasters")String amountPiasters,@FormParam("refNo")String refNo,@FormParam("reason")String reason){

        String msisdn= SessionUtil.currentMsisdn(request);

        DebitResult debitResult=debitService.debit(msisdn,amountPiasters,refNo,reason);

        JsonObject data= Json.createObjectBuilder()
                .add("TXN_ID",debitResult.txnId())
                .add("BALANCE_PIASTERS",debitResult.balancePiasters())
                .build();

        return ResponseHelper.ok(data);
    }
}
