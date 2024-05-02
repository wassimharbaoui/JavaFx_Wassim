package View;

import Entities.Avis;
import Entities.Evenement;
import Services.AvisService;
import Services.EvenementService;
import Services.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;




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
    @FXML
    private Label jourrestant;
    @FXML
    private Label meilleurevenement;
    private ObservableList<Evenement> evenements = FXCollections.observableArrayList();
    private IService<Evenement> evenementService = new EvenementService();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Label[] starLabels = new Label[5];
    private int selectedNote = 0;

    @FXML
    private void initialize() {
        refreshCardView();
        afficherMeilleurEvenement();
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

        Button donnerAvisButton = new Button("Donner avis");
        donnerAvisButton.setOnAction(event -> openAvisDialog(evenement));

        Button voirAvisButton = new Button("Voir les avis");
        voirAvisButton.setOnAction(event -> showAvisForEvenement(evenement));

        HBox buttonsBox = new HBox(donnerAvisButton, voirAvisButton);
        buttonsBox.setSpacing(10); // Espacement entre les boutons

        VBox cardContent = new VBox(content, buttonsBox); // Ajouter les boutons à la carte

        card.getChildren().addAll(cardContent);
        card.setEffect(new javafx.scene.effect.DropShadow(10, Color.GRAY));

        card.setOnMouseClicked(event -> {
            showEvenementDetails(evenement);
        });

        return card;
    }




    private void showEvenementDetails(Evenement evenement) {
        labelTitre.setText(evenement.getTitre());
        labelDateDebut.setText(dateFormat.format(evenement.getDate_debut())); // Utilisation de java.sql.Date
        labelDateFin.setText(dateFormat.format(evenement.getDate_fin()));
        labelTheme.setText(evenement.getTheme());
        labelLocalisation.setText(evenement.getLocalisation());
        labelDescription.setText(evenement.getDescription());

        // Convertir la date de début en LocalDate
        LocalDate dateDebutLocal = evenement.getDate_debut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Utiliser la date de début en tant que LocalDate
        System.out.println("Date de début (LocalDate) : " + dateDebutLocal);
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
            stage.setTitle("Ajouter un nouveau ticket");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            showAlertDialog(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement du formulaire", e.getMessage());
        }
    }
    private void openAvisDialog(Evenement evenement) {
        // Initialiser les étoiles pour la note
        AnchorPane starPane = new AnchorPane();
        starPane.setPrefHeight(30);
        starPane.setPrefWidth(150);
        initStarRating(starPane);

        // Créez une boîte de dialogue de type INFORMATION
        Alert avisDialog = new Alert(Alert.AlertType.INFORMATION);
        avisDialog.setTitle("Donner avis");
        avisDialog.setHeaderText("Donner un avis pour : " + evenement.getTitre());

        // Créez des champs de saisie pour la note et le commentaire
        TextArea commentaireArea = new TextArea();
        commentaireArea.setPromptText("Commentaire");

        // Ajoutez les champs à la boîte de dialogue
        VBox vbox = new VBox(starPane, commentaireArea);
        vbox.setSpacing(10);
        avisDialog.getDialogPane().setContent(vbox);

        // Ajoutez des boutons personnalisés
        avisDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Attendez la réponse de l'utilisateur
        avisDialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // Récupérez les valeurs saisies par l'utilisateur
                String commentaire = commentaireArea.getText();
                int note = selectedNote; // Utilisez la note sélectionnée

                // Créez un nouvel objet Avis avec les données saisies par l'utilisateur
                Avis avis = new Avis();
                avis.setEvent_id(evenement.getId()); // ID de l'événement associé à cet avis
                avis.setNote(note);
                avis.setCommentaire(commentaire);

                // Enregistrez l'avis dans la base de données en utilisant votre service AvisService
                AvisService avisService = new AvisService();
                avisService.create(avis);

                // Affichez un message de confirmation à l'utilisateur
                Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText(null);
                confirmationDialog.setContentText("Avis enregistré avec succès.");
                confirmationDialog.showAndWait();
            }
        });
    }


    private void showAvisForEvenement(Evenement evenement) {
        // Récupérez les avis pour cet événement à l'aide d'un service AvisService
        AvisService avisService = new AvisService();
        List<Avis> avisList = avisService.getAvisForEvenement(evenement.getId());

        // Créez une boîte de dialogue ou une nouvelle fenêtre pour afficher les avis
        // Vous pouvez utiliser une ListView ou tout autre composant pour afficher les avis
        // Voici un exemple de boîte de dialogue simple avec une ListView :
        ListView<String> avisListView = new ListView<>();
        avisList.forEach(avis -> {
            String avisText = "Note: " + avis.getNote() + ", Commentaire: " + avis.getCommentaire();
            avisListView.getItems().add(avisText);
        });

        Alert avisDialog = new Alert(Alert.AlertType.INFORMATION);
        avisDialog.setTitle("Avis pour " + evenement.getTitre());
        avisDialog.setHeaderText(null);
        avisDialog.getDialogPane().setContent(avisListView);
        avisDialog.showAndWait();
    }
    private void initStarRating(AnchorPane parentPane) {
        // Créez des étiquettes pour chaque étoile et configurez leur apparence
        for (int i = 0; i < 5; i++) {
            Label starLabel = new Label("☆");
            starLabel.setFont(new Font("Arial", 24));
            starLabels[i] = starLabel;
            int finalI = i + 1; // Notez que finalI est effectivement une variable finale pour utilisation dans la lambda

            // Gestionnaire pour la survol de la souris
            starLabel.setOnMouseEntered(event -> {
                if (finalI <= selectedNote) {
                    updateStarRating(finalI, "yellow"); // Si la note est déjà sélectionnée, changez la couleur en jaune
                } else {
                    updateStarRating(finalI, "black"); // Sinon, changez la couleur en noir
                }
            });

            // Gestionnaire pour cliquer sur l'étoile
            starLabel.setOnMouseClicked(event -> {
                selectedNote = finalI;
                updateStarRating(finalI, "yellow"); // Mettre à jour la couleur des étoiles jusqu'à la note sélectionnée
            });

            // Ajouter l'étiquette d'étoile au conteneur parent
            parentPane.getChildren().add(starLabel);

            // Définir les positions x et y de l'étoile
            AnchorPane.setLeftAnchor(starLabel, i * 30.0);
            AnchorPane.setTopAnchor(starLabel, 0.0);
        }
    }

    // Méthode pour mettre à jour l'apparence des étoiles jusqu'à la note sélectionnée
    private void updateStarRating(int selectedNote, String color) {
        for (int i = 0; i < 5; i++) {
            if (i < selectedNote) {
                starLabels[i].setText("★"); // Étoile pleine pour les notes sélectionnées
            } else {
                starLabels[i].setText("☆"); // Étoile vide pour les notes non sélectionnées
            }
            starLabels[i].setTextFill(Color.web(color)); // Mettre à jour la couleur de l'étoile
        }
    }


    private void afficherMeilleurEvenement() {
        double meilleureMoyenne = 0;
        Evenement meilleurEvenement = null;

        // Parcourir tous les événements
        for (Evenement evenement : evenements) {
            // Récupérer les avis associés à cet événement
            AvisService avisService = new AvisService();
            List<Avis> avisList = avisService.getAvisForEvenement(evenement.getId());

            // Calculer la moyenne des notes pour cet événement
            double moyenne = calculerMoyenneNotes(avisList);

            // Mettre à jour l'événement avec la meilleure moyenne si nécessaire
            if (moyenne > meilleureMoyenne) {
                meilleureMoyenne = moyenne;
                meilleurEvenement = evenement;
            }
        }

        // Afficher les détails du meilleur événement dans le label "meilleurevenement"
        if (meilleurEvenement != null) {
            meilleurevenement.setText("Meilleur événement : " + meilleurEvenement.getTitre() + ", Moyenne des notes : " + meilleureMoyenne);
        } else {
            meilleurevenement.setText("Aucun événement trouvé");
        }
    }

    private double calculerMoyenneNotes(List<Avis> avisList) {
        if (avisList.isEmpty()) {
            return 0; // Retourner 0 si aucun avis n'est disponible
        }

        double totalNotes = 0;
        for (Avis avis : avisList) {
            totalNotes += avis.getNote();
        }

        return totalNotes / avisList.size(); // Calculer la moyenne
    }



}