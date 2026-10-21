package bsscore.api;

import bsscore.common.ErrorCode;
import bsscore.common.ResponseHelper;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class UnexpectedException implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(UnexpectedException.class.getName());

    @Override
    public Response toResponse(Throwable throwable) {
        if(throwable instanceof WebApplicationException webAppException) {
            return webAppException.getResponse();
        }
        LOG.log(Level.SEVERE, "Unexpected error", throwable);
        return ResponseHelper.fail(ErrorCode.SYS999);
    }
}
