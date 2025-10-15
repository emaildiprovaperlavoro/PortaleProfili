package it.montepaschi.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DbTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/PortaleProfiliDS");
            try (Connection conn = ds.getConnection()) {
                if (conn != null && !conn.isClosed()) {
                    out.write("CONNESSIONE OK");
                } else {
                    out.write("CONNESSIONE FALLITA: connessione nulla o chiusa");
                }
            } catch (Exception e) {
                out.write("ERRORE DB: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        } catch (NamingException ne) {
            throw new ServletException("JNDI lookup failed: " + ne.getMessage(), ne);
        }
    }
}
