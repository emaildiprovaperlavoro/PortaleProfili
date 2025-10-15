package viewmodel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import dao.ProfiliAbilitazioniDao;
import model.ProfiliAbilitazioni;

public class CreateProfileViewModel {

    private ProfiliAbilitazioni profile;
    private ProfiliAbilitazioniDao dao = new ProfiliAbilitazioniDao();

    public ProfiliAbilitazioni getProfile() { return profile; }

    @Init
    public void init() {
        profile = new ProfiliAbilitazioni();
    }

    @Command
    public void save() {
        try {
            // insert nel DB (assicurati che idProfilo sia valorizzato o modificare schema per identity)
            dao.insert(profile);
            Clients.showNotification("Salvato con successo", "info", null, "middle_center", 2000);
            Executions.sendRedirect("home.zul");
        } catch (Exception e) {
            Clients.showNotification("Errore salvataggio: " + e.getMessage(), "error", null, "middle_center", 4000);
        }
    }

    @Command
    public void cancel() {
        Executions.sendRedirect("home.zul");
    }
}
