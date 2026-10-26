package bsscore.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil {

    private static final String ATTRIBUTE_USER = "user";
    private static final String ATTRIBUTE_ROLE = "role";

    private SessionUtil() {}

    public static void login(HttpServletRequest request,
                             String userId, Role role) {
        HttpSession old = request.getSession(false);
        if(old != null) {
            old.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(ATTRIBUTE_USER, userId);
        session.setAttribute(ATTRIBUTE_ROLE, role);
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
    }

    public static Role currentRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Role) session.getAttribute(ATTRIBUTE_ROLE);
    }

    public static String currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (String) session.getAttribute(ATTRIBUTE_USER);
    }

    public static String currentMsisdn(HttpServletRequest request) {
        return currentRole(request) == Role.SUB ? currentUser(request) : null;
    }
}
