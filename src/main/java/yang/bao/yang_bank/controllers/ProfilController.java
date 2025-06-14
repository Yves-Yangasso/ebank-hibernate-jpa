package yang.bao.yang_bank.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.service.CustomerService;

public class ProfilController {

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnRetour;

    private CustomerService customerService;
    private Custumer currentCustomer;

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setCurrentCustomer(Custumer customer) {
        this.currentCustomer = customer;
        loadProfile();
    }

    @FXML
    private void initialize() {
        // Chargement initial du profil géré par setCurrentCustomer
    }

    private void loadProfile() {
        if (currentCustomer != null) {
            txtFirstName.setText(currentCustomer.getFirstname());
            txtLastName.setText(currentCustomer.getLastname());
            txtUsername.setText(currentCustomer.getUsername());
            txtPassword.clear();
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Prénom, nom et nom d'utilisateur sont requis.");
            return;
        }

        try {
            currentCustomer.setFirstname(firstName);
            currentCustomer.setLastname(lastName);
            currentCustomer.setUsername(username);
            if (!password.isEmpty()) {
                currentCustomer.setPassword(password);
            }
            customerService.updateCustomer(currentCustomer);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HisTransction.fxml"));
            Parent root = loader.load();
            TransactionController controller = loader.getController();
            controller.setCustomerService(customerService);
            controller.setCurrentCustomer(currentCustomer);
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Historique des Transactions");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à l'historique.");
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