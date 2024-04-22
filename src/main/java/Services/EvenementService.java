package Services;

import Entities.Evenement;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvenementService implements IService<Evenement> {
    private Connection conn;

    public EvenementService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void insert(Evenement o) {
        String requete = "INSERT INTO event (titre, theme, localisation, description, date_debut, date_fin) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, o.getTitre());
            pst.setString(2, o.getTheme());
            pst.setString(3, o.getLocalisation());
            pst.setString(4, o.getDescription());
            pst.setDate(5, new java.sql.Date(o.getDate_debut().getTime()));
            pst.setDate(6, new java.sql.Date(o.getDate_fin().getTime()));
            pst.executeUpdate();
            System.out.println("Événement ajouté avec succès !");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Evenement o) {
        String requete = "UPDATE event SET titre=?, theme=?, localisation=?, description=?, date_debut=?, date_fin=? WHERE id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, o.getTitre());
            pst.setString(2, o.getTheme());
            pst.setString(3, o.getLocalisation());
            pst.setString(4, o.getDescription());
            pst.setDate(5, new java.sql.Date(o.getDate_debut().getTime()));
            pst.setDate(6, new java.sql.Date(o.getDate_fin().getTime()));
            pst.setInt(7, o.getId());
            pst.executeUpdate();
            System.out.println("Événement mis à jour avec succès !");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(int id) {
        String requete1="DELETE FROM ticket WHERE event_id = ?";

        try {
            PreparedStatement pst1 = conn.prepareStatement(requete1);
            pst1.setInt(1, id);
            pst1.executeUpdate();
            System.out.println("Tickets de l'event supprimés avec succès !");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }



        String requete = "DELETE FROM event WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Événement supprimé avec succès !");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public Evenement readById(int id) {
        String requete = "SELECT * FROM event WHERE id = ?";
        Evenement e = null;
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                e = new Evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("theme"),
                        rs.getString("localisation"),
                        rs.getString("description"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return e;
    }

    @Override
    public ArrayList<Evenement> readAll() {
        String requete = "SELECT * FROM event";
        ArrayList<Evenement> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Evenement e = new Evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("theme"),
                        rs.getString("localisation"),
                        rs.getString("description"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin")
                );
                list.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
