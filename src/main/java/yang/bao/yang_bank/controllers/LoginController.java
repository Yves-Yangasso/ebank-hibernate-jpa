package yang.bao.yang_bank.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.service.CustomerService;
import yang.bao.yang_bank.service.impl.CustomerServiceImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txt_username; // Correspond à fx:id="txt_username"

    @FXML
    private TextField txt_mdp; // Correspond à fx:id="txt_mdp"

    @FXML
    private Button btn_inscription; // Correspond à fx:id="btn_inscription"

    @FXML
    private Button btn_connexion; // Correspond à fx:id="btn_connexion"

    private EntityManagerFactory emf;
    private EntityManager em;
    private CustomerService customerService;

    @FXML
    public void initialize() {
        // Initialisation des ressources JPA
        emf = Persistence.createEntityManagerFactory("yang_bank_pu");
        em = emf.createEntityManager();
        customerService = new CustomerServiceImpl(em);
    }

    @FXML
    private void handleConnexion(ActionEvent event) {
        if (txt_username == null || txt_mdp == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les champs ne sont pas correctement chargés.");
            return;
        }

        String username = txt_username.getText();
        String password = txt_mdp.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nom d'utilisateur et un mot de passe.");
            return;
        }

        Custumer customer = customerService.getCustomerByUsername(username);

        if (customer != null && customer.getPassword().equals(password)) {
            getFormHisTransction(event, customer);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/inscription.fxml"));
            Parent root = loader.load();
            Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (stageActuel != null) {
                stageActuel.setScene(new Scene(root));
                stageActuel.setTitle("Inscription");
                stageActuel.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'accéder à la fenêtre actuelle.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger le formulaire d'inscription.");
        }
    }

    private void getFormHisTransction(ActionEvent event, Custumer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HisTransction.fxml"));
            Parent root = loader.load();
            Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (stageActuel != null) {
                stageActuel.setScene(new Scene(root));
                stageActuel.setTitle("Historique des Transactions");
                stageActuel.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'accéder à la fenêtre actuelle.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'historique des transactions : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Nettoyage des ressources JPA
    public void shutdown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }


}