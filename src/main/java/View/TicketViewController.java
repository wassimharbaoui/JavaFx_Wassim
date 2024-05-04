package View;

import Entities.Ticket;
import Services.TicketService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class TicketViewController {
    // Vos autres champs et méthodes ici...

    @FXML
    private TableView<Ticket> ticketTable;
    @FXML
    private TableColumn<Ticket, Integer> idColumn;
    @FXML
    private TableColumn<Ticket, Integer> userIdColumn;
    @FXML
    private TableColumn<Ticket, Integer> eventIdColumn;
    @FXML
    private TableColumn<Ticket, Double> prixColumn;
    @FXML
    private TableColumn<Ticket, String> typeColumn;

    private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();



    private int id;

    private TicketService ticketService = new TicketService();

    @FXML
    public void initialize() {
        // Initialisez la table avec les colonnes et le modèle de données
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        userIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUser_id()));
        eventIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEvent_id()));
        prixColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrix()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType_ticket()));

        // Ajoutez la liste observable à la table
        ticketTable.setItems(ticketData);
    }



    public void loadTickets(int eventId) {
        try {
            List<Ticket> tickets = ticketService.getAllByEventId(eventId);

            // Vérifier s'il y a plus d'un ticket pour cet événement
            if (tickets.size() > 1) {
                // Regrouper les informations de tous les tickets dans une seule chaîne
                StringBuilder ticketInfoBuilder = new StringBuilder();
                for (Ticket ticket : tickets) {
                    ticketInfoBuilder.append("Ticket ID: ").append(ticket.getId()).append("\n")
                            .append("Type: ").append(ticket.getType_ticket()).append("\n")
                            .append("Prix: ").append(ticket.getPrix()).append("\n\n");
                }
                String ticketInfo = ticketInfoBuilder.toString().trim(); // Supprimer l'excédent de saut de ligne

                // Générer le code QR pour les informations de tous les tickets
                ImageView qrView = new ImageView(QRCodeGenerator.generateQRCodeImage(tickets));
                qrView.setFitHeight(200);
                qrView.setFitWidth(200);

                // Afficher le code QR dans une boîte de dialogue
                Alert qrDialog = new Alert(Alert.AlertType.INFORMATION);
                qrDialog.setTitle("QR Code pour les tickets de l'événement ID: " + eventId);
                qrDialog.setHeaderText(null);
                qrDialog.setGraphic(qrView);
                qrDialog.showAndWait();
            } else {
                // S'il n'y a qu'un seul ticket, affiche le QR code pour ce ticket uniquement
                for (Ticket ticket : tickets) {
                    ImageView qrView = new ImageView(QRCodeGenerator.generateQRCodeImage(tickets));
                    qrView.setFitHeight(200);
                    qrView.setFitWidth(200);

                    Alert qrDialog = new Alert(Alert.AlertType.INFORMATION);
                    qrDialog.setTitle("QR Code pour le ticket ID: " + ticket.getId());
                    qrDialog.setHeaderText(null);
                    qrDialog.setGraphic(qrView);
                    qrDialog.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération du QR code", e.getMessage());
        }
        try {
            // Récupérez les tickets pour l'événement donné
            List<Ticket> tickets = ticketService.getAllByEventId(eventId);

            // Ajoutez les tickets récupérés à la liste observable
            ticketData.addAll(tickets);

            // Rafraîchissez la table avec les nouveaux tickets
            ticketTable.refresh();


        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des tickets", e.getMessage());
        }
    }

    // Vos autres méthodes ici...



    public void handleDeleteTicket() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            ticketService.delete(selectedTicket.getId());
            refreshTableView(); // Rafraîchit la table après la suppression
        } else {
            showAlertDialog(Alert.AlertType.WARNING, "No Selection", "No Ticket Selected", "Please select a ticket from the table to delete.");
        }
    }

    private void refreshTableView() {
        ObservableList<Ticket> tickets = FXCollections.observableArrayList(ticketService.getAllByEventId(id));
        ticketTable.setItems(tickets);
    }

    private void showAlertDialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
