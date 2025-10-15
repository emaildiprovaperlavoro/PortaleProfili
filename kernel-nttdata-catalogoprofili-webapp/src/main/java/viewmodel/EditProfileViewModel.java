package viewmodel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import dao.ProfiliAbilitazioniDao;
import model.ProfiliAbilitazioni;

public class EditProfileViewModel {

    private ProfiliAbilitazioni profile;
    private ProfiliAbilitazioniDao dao = new ProfiliAbilitazioniDao();

    public ProfiliAbilitazioni getProfile() { return profile; }

    @Init
    public void init() {
        String idStr = Executions.getCurrent().getParameter("id");
        try {
            if (idStr != null) {
                Integer id = Integer.valueOf(idStr);
                profile = dao.findById(id);
            }
        } catch (Exception e) {
            Clients.showNotification("Errore caricamento: " + e.getMessage(), "error", null, "middle_center", 4000);
        }
        if (profile == null) {
            profile = new ProfiliAbilitazioni(); // fallback
        }
    }

    @Command
    public void save() {
        try {
            dao.update(profile);
            Clients.showNotification("Profilo aggiornato", "info", null, "middle_center", 2000);
            Executions.sendRedirect("home.zul");
        } catch (Exception e) {
            Clients.showNotification("Errore update: " + e.getMessage(), "error", null, "middle_center", 4000);
        }
    }

    @Command
    public void cancel() {
        Executions.sendRedirect("home.zul");
    }
}
