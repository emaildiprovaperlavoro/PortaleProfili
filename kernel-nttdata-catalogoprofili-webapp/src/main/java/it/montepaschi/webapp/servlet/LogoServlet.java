package it.montepaschi.webapp.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoServlet extends HttpServlet {
    // Percorso assoluto al file logo.png (usalo così come l'hai indicato)
    private static final String LOGO_PATH = "C:\\Users\\Antonio\\workspaces\\PortaleProfili\\kernel-nttdata-catalogoprofili-webapp\\logo.png";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File f = new File(LOGO_PATH);
        if (!f.exists() || !f.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Logo non trovato: " + LOGO_PATH);
            return;
        }
        String mime = getServletContext().getMimeType(f.getName());
        if (mime == null) {
            // for images prefer image/png fallback
            mime = "image/png";
        }
        resp.setContentType(mime);
        resp.setContentLengthLong(f.length());
        try (FileInputStream in = new FileInputStream(f);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }
}
