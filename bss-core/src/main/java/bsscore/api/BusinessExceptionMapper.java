package bsscore.api;

import bsscore.common.BusinessException;
import bsscore.common.ResponseHelper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException exception) {
        return ResponseHelper.fail(exception.getErrorCode());
    }
}
