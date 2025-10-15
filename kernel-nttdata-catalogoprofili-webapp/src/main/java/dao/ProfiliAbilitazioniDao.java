package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.ProfiliAbilitazioni;

public class ProfiliAbilitazioniDao {

    private DataSource getDS() throws Exception {
        InitialContext ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/PortaleProfiliDS");
    }

    public List<ProfiliAbilitazioni> listAll() throws Exception {
        List<ProfiliAbilitazioni> out = new ArrayList<>();
        String sql = "SELECT * FROM profili_abilitazioni ORDER BY id_profilo";
        try (Connection c = getDS().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProfiliAbilitazioni p = mapRow(rs);
                out.add(p);
            }
        }
        return out;
    }

    public void insert(ProfiliAbilitazioni p) throws Exception {
        String sql = "INSERT INTO profili_abilitazioni (id_profilo, nome_abilitazione, sistema_target, descrizione_breve, descrizione_estesa, aggregato, tipologia, apm, grants, gadis, gadis_tecnologia, gadis_approvatore, ats, ambiente_prod, ambiente_npe, azienda, impatti_su_cono_visibilita, formazione, licenze, licenza_resp, ad_personam, ricertificabile, wf_assegnazione_ap, toxic_profile, add_profile, utenti_impattati, funzioni_di_business, verificato_it, session_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = getDS().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, p, 1);
            ps.executeUpdate();
        }
    }

    public void update(ProfiliAbilitazioni p) throws Exception {
        String sql = "UPDATE profili_abilitazioni SET nome_abilitazione=?, sistema_target=?, descrizione_breve=?, descrizione_estesa=?, aggregato=?, tipologia=?, apm=?, grants=?, gadis=?, gadis_tecnologia=?, gadis_approvatore=?, ats=?, ambiente_prod=?, ambiente_npe=?, azienda=?, impatti_su_cono_visibilita=?, formazione=?, licenze=?, licenza_resp=?, ad_personam=?, ricertificabile=?, wf_assegnazione_ap=?, toxic_profile=?, add_profile=?, utenti_impattati=?, funzioni_di_business=?, verificato_it=?, session_id=? WHERE id_profilo=?";
        try (Connection c = getDS().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, p, 1);
            // last param = id_profilo for WHERE
            ps.setObject(29, p.getIdProfilo());
            ps.executeUpdate();
        }
    }

    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM profili_abilitazioni WHERE id_profilo=?";
        try (Connection c = getDS().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    public ProfiliAbilitazioni findById(Integer id) throws Exception {
        if (id == null) return null;
        String sql = "SELECT * FROM profili_abilitazioni WHERE id_profilo = ?";
        try (Connection c = getDS().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    private ProfiliAbilitazioni mapRow(ResultSet rs) throws Exception {
        ProfiliAbilitazioni p = new ProfiliAbilitazioni();
        p.setIdProfilo(rs.getObject("id_profilo") != null ? rs.getInt("id_profilo") : null);
        p.setNomeAbilitazione(rs.getString("nome_abilitazione"));
        p.setSistemaTarget(rs.getString("sistema_target"));
        p.setDescrizioneBreve(rs.getString("descrizione_breve"));
        p.setDescrizioneEstesa(rs.getString("descrizione_estesa"));
        p.setAggregato(rs.getString("aggregato"));
        p.setTipologia(rs.getString("tipologia"));
        p.setApm(rs.getString("apm"));
        p.setGrant(rs.getObject("grants") != null ? rs.getBoolean("grants") : null);
        p.setGadis(rs.getObject("gadis") != null ? rs.getBoolean("gadis") : null);
        p.setGadisTecnologia(rs.getString("gadis_tecnologia"));
        p.setGadisApprov(rs.getObject("gadis_approvatore") != null ? rs.getBoolean("gadis_approvatore") : null);
        p.setAts(rs.getString("ats"));
        p.setAmbienteProd(rs.getObject("ambiente_prod") != null ? rs.getBoolean("ambiente_prod") : null);
        p.setAmbienteNpe(rs.getObject("ambiente_npe") != null ? rs.getBoolean("ambiente_npe") : null);
        p.setAzienda(rs.getString("azienda"));
        p.setImpattiSuConoVisibilita(rs.getString("impatti_su_cono_visibilita"));
        p.setFormazione(rs.getObject("formazione") != null ? rs.getBoolean("formazione") : null);
        p.setLicenze(rs.getObject("licenze") != null ? rs.getBoolean("licenze") : null);
        p.setLicenzaResp(rs.getString("licenza_resp"));
        p.setAdPersonam(rs.getObject("ad_personam") != null ? rs.getBoolean("ad_personam") : null);
        p.setRicertificabile(rs.getString("ricertificabile"));
        p.setWfAssegnazioneAp(rs.getObject("wf_assegnazione_ap") != null ? rs.getBoolean("wf_assegnazione_ap") : null);
        p.setToxicProfile(rs.getObject("toxic_profile") != null ? rs.getBoolean("toxic_profile") : null);
        p.setAddProfile(rs.getObject("add_profile") != null ? rs.getBoolean("add_profile") : null);
        p.setUtentiImpattati(rs.getObject("utenti_impattati") != null ? rs.getInt("utenti_impattati") : null);
        p.setFunzioniDiBusiness(rs.getString("funzioni_di_business"));
        p.setVerificatoIt(rs.getObject("verificato_it") != null ? rs.getBoolean("verificato_it") : null);
        p.setSessionId(rs.getObject("session_id") != null ? rs.getInt("session_id") : null);
        return p;
    }

    private void setParams(PreparedStatement ps, ProfiliAbilitazioni p, int startIndex) throws Exception {
        ps.setObject(startIndex++, p.getNomeAbilitazione());
        ps.setObject(startIndex++, p.getSistemaTarget());
        ps.setObject(startIndex++, p.getDescrizioneBreve());
        ps.setObject(startIndex++, p.getDescrizioneEstesa());
        ps.setObject(startIndex++, p.getAggregato());
        ps.setObject(startIndex++, p.getTipologia());
        ps.setObject(startIndex++, p.getApm());
        ps.setObject(startIndex++, p.getGrant());
        ps.setObject(startIndex++, p.getGadis());
        ps.setObject(startIndex++, p.getGadisTecnologia());
        ps.setObject(startIndex++, p.getGadisApprov());
        ps.setObject(startIndex++, p.getAts());
        ps.setObject(startIndex++, p.getAmbienteProd());
        ps.setObject(startIndex++, p.getAmbienteNpe());
        ps.setObject(startIndex++, p.getAzienda());
        ps.setObject(startIndex++, p.getImpattiSuConoVisibilita());
        ps.setObject(startIndex++, p.getFormazione());
        ps.setObject(startIndex++, p.getLicenze());
        ps.setObject(startIndex++, p.getLicenzaResp());
        ps.setObject(startIndex++, p.getAdPersonam());
        ps.setObject(startIndex++, p.getRicertificabile());
        ps.setObject(startIndex++, p.getWfAssegnazioneAp());
        ps.setObject(startIndex++, p.getToxicProfile());
        ps.setObject(startIndex++, p.getAddProfile());
        ps.setObject(startIndex++, p.getUtentiImpattati());
        ps.setObject(startIndex++, p.getFunzioniDiBusiness());
        ps.setObject(startIndex++, p.getVerificatoIt());
        ps.setObject(startIndex++, p.getSessionId());
        // note: for update we set WHERE id_profilo separately
    }
}
