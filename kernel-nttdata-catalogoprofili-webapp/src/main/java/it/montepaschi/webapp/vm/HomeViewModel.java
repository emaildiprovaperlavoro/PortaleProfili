package it.montepaschi.webapp.vm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import it.montepaschi.webapp.model.Profile;

public class HomeViewModel {

    private List<Profile> profiles = new ArrayList<>();
    private String filter = "";

    public List<Profile> getProfiles() { return profiles; }
    public String getFilter() { return filter; }
    public void setFilter(String filter) { this.filter = filter; }

    @Init
    public void init() {
        // opzionale: caricare inizialmente
    }

    @Command
    @NotifyChange("profiles")
    public void loadProfiles() {
        List<Profile> list = new ArrayList<>();
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/PortaleProfiliDS");
            try (Connection conn = ds.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT id_toxic_profile AS id, '' AS first_name, '' AS last_name, '' AS email, session_id AS role FROM toxic_profile"
                 )) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Profile p = new Profile();
                        p.setId(rs.getLong("id"));
                        p.setFirstName(rs.getString("first_name"));
                        p.setLastName(rs.getString("last_name"));
                        p.setEmail(rs.getString("email"));
                        p.setRole(rs.getString("role"));
                        list.add(p);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento profili: " + e.getMessage());
            e.printStackTrace();
        }
        this.profiles = list;
    }

    @Command
    @NotifyChange("profiles")
    public void applyFilter() {
        if (filter == null || filter.trim().isEmpty()) {
            loadProfiles();
            return;
        }
        List<Profile> filtered = new ArrayList<>();
        for (Profile p : profiles) {
            if ((p.getFirstName() != null && p.getFirstName().toLowerCase().contains(filter.toLowerCase()))
             || (p.getLastName() != null && p.getLastName().toLowerCase().contains(filter.toLowerCase()))
             || (p.getEmail() != null && p.getEmail().toLowerCase().contains(filter.toLowerCase()))) {
                filtered.add(p);
            }
        }
        this.profiles = filtered;
    }
}
