package View;

import Entities.Evenement;
import Services.EvenementService;
import Services.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;

public class FrontIndexEvenementController {

    @FXML
    private VBox cardViewContainer;
    @FXML
    private ScrollPane cardViewScrollPane;
    @FXML
    private Label labelTitre;
    @FXML
    private Label labelDateDebut;
    @FXML
    private Label labelDateFin;
    @FXML
    private Label labelTheme;
    @FXML
    private Label labelLocalisation;
    @FXML
    private Label labelDescription;

    private ObservableList<Evenement> evenements = FXCollections.observableArrayList();
    private IService<Evenement> evenementService = new EvenementService();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    private void initialize() {
        refreshCardView();
    }

    public void refreshCardView() {
        cardViewContainer.getChildren().clear();
        evenements.clear();
        evenements.addAll(evenementService.readAll());

        for (Evenement evenement : evenements) {
            AnchorPane card = createCardView(evenement);
            cardViewContainer.getChildren().add(card);
        }
    }

    private AnchorPane createCardView(Evenement evenement) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("card-view");

        Label titreLabel = new Label(evenement.getTitre());
        titreLabel.setFont(new Font("System Bold", 16));

        Label dateDebutLabel = new Label("Date de début: " + dateFormat.format(evenement.getDate_debut()));
        Label dateFinLabel = new Label("Date de fin: " + dateFormat.format(evenement.getDate_fin()));
        Label themeLabel = new Label("Thème: " + evenement.getTheme());
        Label localisationLabel = new Label("Localisation: " + evenement.getLocalisation());
        Label descriptionLabel = new Label("Description: " + evenement.getDescription());

        VBox content = new VBox(titreLabel, dateDebutLabel, dateFinLabel, themeLabel, localisationLabel, descriptionLabel);
        content.setSpacing(5);
        content.setPadding(new Insets(10));

        card.getChildren().add(content);
        card.setEffect(new javafx.scene.effect.DropShadow(10, Color.GRAY));

        card.setOnMouseClicked(event -> {
            showEvenementDetails(evenement);
        });

        return card;
    }

    private void showEvenementDetails(Evenement evenement) {
        labelTitre.setText(evenement.getTitre());
        labelDateDebut.setText(dateFormat.format(evenement.getDate_debut()));
        labelDateFin.setText(dateFormat.format(evenement.getDate_fin()));
        labelTheme.setText(evenement.getTheme());
        labelLocalisation.setText(evenement.getLocalisation());
        labelDescription.setText(evenement.getDescription());
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
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
