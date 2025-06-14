package yang.bao.yang_bank.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.service.AccountService;

import java.time.Instant;

public class ComptesController {

    @FXML
    private TableView<Account> tableAccounts;

    @FXML
    private TableColumn<Account, Integer> colId;

    @FXML
    private TableColumn<Account, Double> colBalance;

    @FXML
    private TableColumn<Account, Instant> colCreationDate;

    @FXML
    private TextField txtInitialBalance;

    @FXML
    private Button btnCreateAccount;

    @FXML
    private Button btnDeconnexion;

    private AccountService accountService;
    private Custumer currentCustomer;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setCurrentCustomer(Custumer customer) {
        this.currentCustomer = customer;
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        loadAccounts();
    }

    private void loadAccounts() {
        if (accountService != null && currentCustomer != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(
                    accountService.getAccountsByCustomer(currentCustomer)
            );
            tableAccounts.setItems(accounts);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les comptes.");
        }
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        String balanceText = txtInitialBalance.getText();
        try {
            double initialBalance = Double.parseDouble(balanceText);
            if (initialBalance < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le solde initial ne peut pas être négatif.");
                return;
            }
            accountService.createAccount(currentCustomer, initialBalance);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès.");
            txtInitialBalance.clear();
            loadAccounts();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un solde valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la création du compte : " + e.getMessage());
        }
    }

    @FXML
    private void handleDeconnexion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnDeconnexion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de revenir à la connexion.");
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