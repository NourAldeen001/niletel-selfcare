package bsscore.api;

import bsscore.common.ResponseHelper;
import bsscore.model.RefundResult;
import bsscore.security.SessionUtil;
import bsscore.service.RefundService;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class RefundResource {
    @EJB
    private RefundService refundService;
    @Context
    private HttpServletRequest servletRequest;

    @POST
    @Path("/subscriber/me/refund")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response refund(@FormParam("refNo")String refNo){

        String msisdn= SessionUtil.currentMsisdn(servletRequest);
        RefundResult refundResult=refundService.refund(msisdn,refNo);

        JsonObject data= Json.createObjectBuilder()
                .add("TXN_ID",refundResult.txnId())
                .add("AMOUNT_PIASTERS",refundResult.amountPiasters())
                .add("BALANCE_PIASTERS",refundResult.balancePiasters())
                .build();

        return ResponseHelper.ok(data);
    }
}
