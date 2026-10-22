package bsscore.filter;

import bsscore.common.ErrorCode;
import bsscore.common.ResponseHelper;
import bsscore.security.Role;
import bsscore.security.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@WebFilter("/api/*")
public class SessionFilter implements Filter {

    private static final Set<Role> ANY_ROLE = EnumSet.allOf(Role.class);
    private static final Set<Role> SUBSCRIBER_ONLY = EnumSet.of(Role.SUB);
    private static final Set<Role> STAFF_ONLY = EnumSet.of(Role.OPR, Role.SYS);

    private static final Set<String> OPEN_PATHS =
            Set.of("/api/auth/login", "/api/auth/operator-login");

    private static final Map<String, Set<Role>> RULES =
            Map.of(
                    "/api/auth/logout", ANY_ROLE,
                    "/api/tariffs", ANY_ROLE,
                    "/api/subscriber/me", SUBSCRIBER_ONLY,
                    "/api/subscribers", STAFF_ONLY
            );

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = requestPath(req);

        if(OPEN_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        Role role = SessionUtil.currentRole(req);
        if(role == null) {
            ResponseHelper.write(resp, ErrorCode.SES001);
            return;
        }

        Set<Role> allowed = allowedRoles(path);
        if(allowed == null || !allowed.contains(role)) {
            ResponseHelper.write(resp, ErrorCode.SEC001);
            return;
        }

        chain.doFilter(request, response);
    }


    private static String requestPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        return req.getServletPath() + (pathInfo == null ? "" : pathInfo);
    }

    private static Set<Role> allowedRoles(String path) {
        for(Map.Entry<String, Set<Role>> rule : RULES.entrySet()) {
            String prefix = rule.getKey();
            if(path.equals(prefix) || path.startsWith(prefix + "/")) {
                return rule.getValue();
            }
        }
        return null;
    }
}
