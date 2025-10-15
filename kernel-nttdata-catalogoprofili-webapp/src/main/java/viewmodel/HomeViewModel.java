package viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import dao.ProfiliAbilitazioniDao;
import model.ProfiliAbilitazioni;

public class HomeViewModel {

    private List<ProfiliAbilitazioni> profiles = new ArrayList<>();
    private ProfiliAbilitazioni selectedProfile = new ProfiliAbilitazioni();
    private ProfiliAbilitazioni newProfile = new ProfiliAbilitazioni();
    private String messaggio = "";

    @Wire
    private Div mainArea;

    public List<ProfiliAbilitazioni> getProfiles() { return profiles; }
    public ProfiliAbilitazioni getSelectedProfile() { return selectedProfile; }
    public void setSelectedProfile(ProfiliAbilitazioni selectedProfile) { this.selectedProfile = selectedProfile; }
    public ProfiliAbilitazioni getNewProfile() { return newProfile; }
    public String getMessaggio() { return messaggio; }

    private ProfiliAbilitazioniDao dao = new ProfiliAbilitazioniDao();

    @Init
    public void init() {
        loadProfiles();
    }

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange({"profiles","messaggio"})
    public void loadProfiles() {
        try {
            profiles = dao.listAll();
            messaggio = "Caricati " + (profiles != null ? profiles.size() : 0) + " record";
        } catch (Exception e) {
            messaggio = "Errore caricamento: " + e.getMessage();
            profiles = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange({"profiles","messaggio","newProfile"})
    public void inserisci() {
        try {
            // newProfile.idProfilo deve essere valorizzato dall'utente (schema richiede PK non serial)
            dao.insert(newProfile);
            messaggio = "Inserimento OK";
            newProfile = new ProfiliAbilitazioni(); // reset form
            loadProfiles();
            Clients.showNotification("Inserito", "info", null, "middle_center", 2000);
        } catch (Exception e) {
            messaggio = "Errore inserimento: " + e.getMessage();
        }
    }

    @Command
    @NotifyChange({"profiles","messaggio"})
    public void modifica() {
        try {
            if (selectedProfile == null || selectedProfile.getIdProfilo() == null) {
                messaggio = "Seleziona un profilo da modificare";
                return;
            }
            dao.update(selectedProfile);
            messaggio = "Modifica OK";
            loadProfiles();
            Clients.showNotification("Modificato", "info", null, "middle_center", 2000);
        } catch (Exception e) {
            messaggio = "Errore modifica: " + e.getMessage();
        }
    }

    @Command
    @NotifyChange({"profiles","messaggio"})
    public void elimina() {
        try {
            if (selectedProfile == null || selectedProfile.getIdProfilo() == null) {
                messaggio = "Seleziona un profilo da eliminare";
                return;
            }
            dao.delete(selectedProfile.getIdProfilo());
            messaggio = "Cancellazione OK";
            selectedProfile = new ProfiliAbilitazioni();
            loadProfiles();
            Clients.showNotification("Eliminato", "info", null, "middle_center", 2000);
        } catch (Exception e) {
            messaggio = "Errore cancellazione: " + e.getMessage();
        }
    }

    @Command
    public void openEdit(@BindingParam("id") Integer id) {
        // reindirizza alla pagina di modifica passando l'id
        Executions.sendRedirect("edit.zul?id=" + id);
    }

    @Command
    public void deleteById(@BindingParam("id") final Integer id) {
        if (id == null) {
            Clients.showNotification("ID non valido", "error", null, "middle_center", 2000);
            return;
        }
        // conferma con Messagebox
        Messagebox.show("Confermi l'eliminazione del profilo ID " + id + "?", "Conferma eliminazione",
                Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
            @Override
            public void onEvent(Event evt) throws Exception {
                Object data = evt.getData(); // dovrebbe essere Integer con valore YES/NO
                if (data instanceof Integer && ((Integer) data).intValue() == Messagebox.YES) {
                    try {
                        dao.delete(id);
                        loadProfiles();
                        Clients.showNotification("Profilo eliminato", "info", null, "middle_center", 2000);
                    } catch (Exception e) {
                        Clients.showNotification("Errore eliminazione: " + e.getMessage(), "error", null, "middle_center", 4000);
                    }
                }
            }
        });
    }
}
