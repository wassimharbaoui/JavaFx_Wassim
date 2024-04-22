package View;

import Entities.Evenement;
import Utils.DateUtils;
import Services.EvenementService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateEvenementController {

    @FXML
    private TextField descriptionField;
    @FXML
    private TextField titreField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private TextField themeField;
    @FXML
    private TextField localisationField;

    private IndexEvenementController indexEvenementController;
    private Evenement evenement;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        loadEvenementData();
    }

    public void setIndexEvenementController(IndexEvenementController controller) {
        this.indexEvenementController = controller;
    }

    private void loadEvenementData() {
        titreField.setText(evenement.getTitre());
        dateDebutPicker.setValue(DateUtils.convertToLocalDateUsingCalendar(evenement.getDate_debut()));
        dateFinPicker.setValue(DateUtils.convertToLocalDateUsingCalendar(evenement.getDate_fin()));

        themeField.setText(evenement.getTheme());
        localisationField.setText(evenement.getLocalisation());
        descriptionField.setText(evenement.getDescription());
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
        } else if (dateFinPicker.getValue().isBefore(dateDebutPicker.getValue())) {
            errorMessage += "La date de fin ne peut pas être avant la date de début !\n";
        }

        if (themeField.getText() == null || themeField.getText().isEmpty()) {
            errorMessage += "Le champ thème est vide !\n";
        }

        if (localisationField.getText() == null || localisationField.getText().isEmpty()) {
            errorMessage += "Le champ localisation est vide !\n";
        }

        if (!errorMessage.isEmpty()) {
            showAlertDialog(Alert.AlertType.ERROR, "Erreur de validation", "Veuillez corriger les erreurs suivantes", errorMessage);
            return false;
        }
        return true;
    }

    @FXML
    private void handleUpdate() {
        if (isInputValid()) {
            evenement.setTitre(titreField.getText());
            evenement.setDate_debut(java.sql.Date.valueOf(dateDebutPicker.getValue()));
            evenement.setDate_fin(java.sql.Date.valueOf(dateFinPicker.getValue()));
            evenement.setTheme(themeField.getText());
            evenement.setLocalisation(localisationField.getText());
            evenement.setDescription(descriptionField.getText());

            EvenementService service = new EvenementService();
            service.update(evenement);
            indexEvenementController.refreshTableView();

            closeStage();
        }
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) titreField.getScene().getWindow();
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
