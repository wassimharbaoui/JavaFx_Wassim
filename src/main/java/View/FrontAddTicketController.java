package View;

import Entities.Evenement;
import Entities.Ticket;
import Entities.User;
import Services.EvenementService;
import Services.TicketService;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class FrontAddTicketController {
    @FXML private ComboBox<Integer> eventIdComboBox;
    @FXML private ComboBox<Integer> userIdComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField priceField;

    private TicketService ticketService = new TicketService();
    private EvenementService evenementService = new EvenementService();
    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        eventIdComboBox.getItems().setAll(
                evenementService.readAll().stream()
                        .map(Evenement::getId)
                        .collect(Collectors.toList())
        );

        // Récupération et ajout des ID d'utilisateurs à partir de la liste des utilisateurs
        userIdComboBox.getItems().setAll(
                userService.readAll().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())

        );
    }

    @FXML
    public void handleAddTicket() {
        if (isInputValid()) {
            Ticket ticket = new Ticket();
            ticket.setEvent_id(eventIdComboBox.getValue());
            ticket.setUser_id(userIdComboBox.getValue());
            ticket.setType_ticket(typeComboBox.getValue());
            ticket.setPrix(Double.parseDouble(priceField.getText()));

            ticketService.insert(ticket);
            closeStage();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (eventIdComboBox.getValue() == null) {
            errorMessage += "No event ID selected!\n";
        }
        if (userIdComboBox.getValue() == null) {
            errorMessage += "No user ID selected!\n";
        }
        if (typeComboBox.getValue() == null) {
            errorMessage += "No ticket type selected!\n";
        }
        if (priceField.getText() == null || priceField.getText().isEmpty()) {
            errorMessage += "Price field is empty!\n";
        } else {
            try {
                Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid price format!\n";
            }
        }

        if (!errorMessage.isEmpty()) {
            showAlertDialog(Alert.AlertType.ERROR, "Invalid Fields", "Please correct invalid fields", errorMessage);
            return false;
        }
        return true;
    }

    private void closeStage() {
        Stage stage = (Stage) priceField.getScene().getWindow();
        stage.close();
    }

    private void showAlertDialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
