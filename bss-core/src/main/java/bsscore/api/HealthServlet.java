package bsscore.api;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {

    @Resource(lookup = "java:jboss/datasources/BssCoreDS")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        try(Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT 1")
        ) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("UP");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write("DOWN");
        }
    }
}
