package View;

import Entities.Evenement;
import Services.EvenementService;
import Services.IService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class FrontIndexEvenementController {

    public Label labelDateFin;
    public Label labelDescription;
    @FXML
    private TableView<Evenement> evenementTable;

    @FXML
    private TableColumn<Evenement, String> titreColumn;

    @FXML
    private TableColumn<Evenement, String> dateDebutColumn;

    @FXML
    private TableColumn<Evenement, String> themeColumn;

    @FXML
    private TableColumn<Evenement, String> localisationColumn;

    @FXML
    private Label labelTitre;

    @FXML
    private Label labelDateDebut;

    @FXML
    private Label labelTheme;

    @FXML
    private Label labelLocalisation;

    private ObservableList<Evenement> evenements = FXCollections.observableArrayList();
    private IService<Evenement> evenementService = new EvenementService();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    private void initialize() {
        titreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        dateDebutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(dateFormat.format(cellData.getValue().getDate_debut())));
        themeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTheme()));
        localisationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalisation()));

        evenementTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEvenementDetails(newValue));

        refreshTableView();
    }

    private void showEvenementDetails(Evenement evenement) {
        if (evenement != null) {
            labelTitre.setText(evenement.getTitre());
            labelDateDebut.setText(dateFormat.format(evenement.getDate_debut()));
            labelDateFin.setText(dateFormat.format(evenement.getDate_fin()));
            labelTheme.setText(evenement.getTheme());
            labelLocalisation.setText(evenement.getLocalisation());
            labelDescription.setText(evenement.getDescription());
        } else {
            labelTitre.setText("");
            labelDateDebut.setText("");
            labelDateFin.setText("");
            labelTheme.setText("");
            labelLocalisation.setText("");
            labelDescription.setText("");

        }}

    @FXML
    public void refreshTableView() {
        try {
            evenements.clear();
            evenements.addAll(evenementService.readAll());
            evenementTable.setItems(evenements);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog(Alert.AlertType.ERROR, "Database Error", "Failed to fetch Evenements", e.getMessage());
        }
    }

    private void showAlertDialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }









    public void launchAddTicketForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/reserver-front.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Ticket");
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Cette méthode bloque jusqu'à ce que la fenêtre soit fermée
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
