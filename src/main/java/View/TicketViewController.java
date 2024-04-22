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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TicketViewController {
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

    private int id;

    private TicketService ticketService = new TicketService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getId()));
        userIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUser_id()));
        eventIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEvent_id()));
        prixColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrix()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType_ticket()));
        //NORMAL,VIP,GOLD,PREMIUM
    }


    public void loadTickets(int eventId) {
        ObservableList<Ticket> tickets = FXCollections.observableArrayList(ticketService.getAllByEventId(eventId));
        ticketTable.setItems(tickets);
        id = eventId;
    }

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
