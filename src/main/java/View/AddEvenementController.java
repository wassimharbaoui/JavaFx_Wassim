package View;

import Entities.Evenement;
import Services.EvenementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEvenementController {
    @FXML
    private TextField titreField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private ComboBox<String> themeComboBox;
    @FXML
    private ComboBox<String> localisationComboBox;
    @FXML
    private TextField descriptionField;

    private IndexEvenementController indexEvenementController;

    public void setIndexEvenementController(IndexEvenementController controller) {
        this.indexEvenementController = controller;
    }

    @FXML
    private void initialize() {
        ObservableList<String> themes = FXCollections.observableArrayList("Voiture", "Motos","Bateaux");
        themeComboBox.setItems(themes);

        ObservableList<String> localisation = FXCollections.observableArrayList(
                "Tunis", "Ariana", "Ben Arous", "Manouba", "Nabeul", "Zaghouan", "Bizerte", "Beja", "Jendouba", "Kef", "Siliana", "Kairouan",
                "Kasserine", "Sidi Bouzid", "Sousse", "Monastir", "Mahdia", "Sfax", "Gafsa", "Tozeur", "Kebili", "Medenine", "Tataouine", "Gabes"
        );
        localisationComboBox.setItems(localisation);
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titreField.getText() == null || titreField.getText().isEmpty()) {
            errorMessage += "Le champ titre est vide !\n";
        }

        if (dateDebutPicker.getValue() == null) {
            errorMessage += "Aucune date de début sélectionnée !\n";
        }

        if (dateFinPicker.getValue() == null) {
            errorMessage += "Aucune date de fin sélectionnée !\n";
        } else if (dateDebutPicker.getValue() != null && dateFinPicker.getValue().isBefore(dateDebutPicker.getValue())) {
            errorMessage += "La date de fin ne peut pas être avant la date de début !\n";
        }

        if (themeComboBox.getValue() == null) {
            errorMessage += "Aucun thème sélectionné !\n";
        }

        if (localisationComboBox.getValue() == null) {
            errorMessage += "Aucune localisation sélectionnée !\n";
        }

        if (!errorMessage.isEmpty()) {
            showAlertDialog(Alert.AlertType.ERROR, "Erreur de validation", "Veuillez corriger les erreurs suivantes", errorMessage);
            return false;
        }
        return true;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            Evenement evenement = new Evenement();
            evenement.setTitre(titreField.getText());
            evenement.setDate_debut(java.sql.Date.valueOf(dateDebutPicker.getValue()));
            evenement.setDate_fin(java.sql.Date.valueOf(dateFinPicker.getValue()));
            evenement.setTheme(themeComboBox.getValue());
            evenement.setLocalisation(localisationComboBox.getValue());
            evenement.setDescription(descriptionField.getText());

            EvenementService service = new EvenementService();
            service.insert(evenement);
            indexEvenementController.refreshTableView();

            closeStage();
        }
    }

    private void closeStage() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void showAlertDialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
