package yang.bao.yang_bank.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.service.CustomerService;

public class LoginController {

    @FXML
    private TextField txt_username;

    @FXML
    private TextField txt_mdp;

    private CustomerService customerService;

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @FXML
    private void handleConnexion(ActionEvent event) {
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
        getFormInscription(event);
    }

    private void getFormInscription(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/inscription.fxml"));
            Parent root = loader.load();
            Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActuel.setScene(new Scene(root));
            stageActuel.setTitle("Inscription");
            stageActuel.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger le formulaire d'inscription.");
        }
    }

    private void getFormHisTransction(ActionEvent event, Custumer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HisTransction.fxml"));
            Parent root = loader.load();
            TransactionController controller = loader.getController();
            controller.setCustomerService(customerService);
            controller.setCurrentCustomer(customer);
            Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActuel.setScene(new Scene(root));
            stageActuel.setTitle("Historique des Transactions");
            stageActuel.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'historique des transactions.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}