package yang.bao.yang_bank.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.service.AccountService;

public class TransfertController {

    @FXML
    private ChoiceBox<Account> cbDepositAccount;

    @FXML
    private ChoiceBox<Account> cbWithdrawAccount;

    @FXML
    private ChoiceBox<Account> cbFromAccount;

    @FXML
    private ChoiceBox<Account> cbToAccount;

    @FXML
    private TextField txtDepositAmount;

    @FXML
    private TextField txtWithdrawAmount;

    @FXML
    private TextField txtTransferAmount;

    @FXML
    private Button btnDeposit;

    @FXML
    private Button btnWithdraw;

    @FXML
    private Button btnTransfer;

    @FXML
    private Button btnDeconnexion;

    private AccountService accountService;
    private Custumer currentCustomer;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setCurrentCustomer(Custumer customer) {
        this.currentCustomer = customer;
        loadAccounts();
    }

    @FXML
    private void initialize() {
        // Chargement des comptes géré par setCurrentCustomer
    }

    private void loadAccounts() {
        if (accountService != null && currentCustomer != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(
                    accountService.getAccountsByCustomer(currentCustomer)
            );
            cbDepositAccount.setItems(accounts);
            cbWithdrawAccount.setItems(accounts);
            cbFromAccount.setItems(accounts);
            cbToAccount.setItems(accounts);
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        Account account = cbDepositAccount.getValue();
        String amountText = txtDepositAmount.getText();
        try {
            if (account == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un compte.");
                return;
            }
            double amount = Double.parseDouble(amountText);
            accountService.deposit(account, amount);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Dépôt effectué avec succès.");
            txtDepositAmount.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un montant valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du dépôt : " + e.getMessage());
        }
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        Account account = cbWithdrawAccount.getValue();
        String amountText = txtWithdrawAmount.getText();
        try {
            if (account == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un compte.");
                return;
            }
            double amount = Double.parseDouble(amountText);
            accountService.withdraw(account, amount);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Retrait effectué avec succès.");
            txtWithdrawAmount.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un montant valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du retrait : " + e.getMessage());
        }
    }

    @FXML
    private void handleTransfer(ActionEvent event) {
        Account fromAccount = cbFromAccount.getValue();
        Account toAccount = cbToAccount.getValue();
        String amountText = txtTransferAmount.getText();
        try {
            if (fromAccount == null || toAccount == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner les comptes source et destination.");
                return;
            }
            double amount = Double.parseDouble(amountText);
            accountService.transfer(fromAccount, toAccount, amount);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Transfert effectué avec succès.");
            txtTransferAmount.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un montant valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du transfert : " + e.getMessage());
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