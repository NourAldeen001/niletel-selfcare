package bsscore.common;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

public final class ResponseHelper {

    private static final String JSON_UTF8 = MediaType.APPLICATION_JSON + ";charset=utf-8";

    private ResponseHelper() {}

    public static Response ok(JsonValue data) {
        JsonObject body = Json.createObjectBuilder()
                .add("RESULT", "OK")
                .add("DATA", data)
                .build();

        return Response.ok(body.toString()).type(JSON_UTF8).build();
    }

    public static Response ok() {
        return ok(JsonValue.EMPTY_JSON_OBJECT);
    }

    private static String failJson(ErrorCode code) {
        return Json.createObjectBuilder()
                .add("RESULT", "FAIL")
                .add("ERR_CODE", code.name())
                .add("ERR_MSG", code.getMessage())
                .build()
                .toString();
    }

    public static Response fail(ErrorCode code) {
        return Response.status(code.getHttpStatus())
                .entity(failJson(code))
                .type(JSON_UTF8)
                .build();
    }

    // For Plain Servlets
    public static void write(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setStatus(code.getHttpStatus());
        response.setContentType(JSON_UTF8);
        response.getWriter().write(failJson(code));
    }

}
