package bsscore.api;

import bsscore.dao.DataAccessException;
import bsscore.dao.HealthDao;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {

    @Inject
    private HealthDao healthDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        try {
            boolean up = healthDao.isDatabaseUp();
            resp.setStatus(up ? HttpServletResponse.SC_OK : HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write(up ? "UP" : "DOWN");
        } catch(DataAccessException ex) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write("DOWN");
        }
    }
}
