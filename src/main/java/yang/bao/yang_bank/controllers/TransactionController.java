package yang.bao.yang_bank.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.entity.Operation;
import yang.bao.yang_bank.service.AccountService;
import yang.bao.yang_bank.service.CustomerService;
import yang.bao.yang_bank.service.OperationService;
import yang.bao.yang_bank.service.impl.AccountServiceImpl;
import yang.bao.yang_bank.service.impl.CustomerServiceImpl;
import yang.bao.yang_bank.service.impl.OperationServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.Instant;

public class TransactionController {

    @FXML
    private TableView<Operation> tableTransactions;

    @FXML
    private TableColumn<Operation, Instant> colDate;

    @FXML
    private TableColumn<Operation, String> colType;

    @FXML
    private TableColumn<Operation, Double> colMontant;

    @FXML
    private TableColumn<Operation, Integer> colCompte;

    @FXML
    private Button btnComptes;

    @FXML
    private Button btnTransfert;

    @FXML
    private Button btnProfil;

    @FXML
    private Button btnDeconnexion;

    @FXML
    private Button btnRafraichir;

    private EntityManagerFactory emf;
    private EntityManager em;
    private CustomerService customerService;
    private AccountService accountService;
    private OperationService operationService;
    private Custumer currentCustomer;

    @FXML
    private void initialize() {
        emf = Persistence.createEntityManagerFactory("yang_bank_pu");
        em = emf.createEntityManager();
        customerService = new CustomerServiceImpl(em);
        accountService  = new AccountServiceImpl(em);
        operationService  = new OperationServiceImpl(em);


        colDate.setCellValueFactory(new PropertyValueFactory<>("operationDate"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeOperation"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colCompte.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAccount().getId()));

        loadTransactions();
    }

    private void loadTransactions() {
        if (operationService != null && accountService != null && currentCustomer != null) {
            ObservableList<Operation> transactions = FXCollections.observableArrayList();
            for (Account account : accountService.getAccountsByCustomer(currentCustomer)) {
                transactions.addAll(operationService.getOperationsByAccount(account));
            }
            tableTransactions.setItems(transactions);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les transactions.");
        }
    }

    @FXML
    private void handleComptes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Comptes.fxml"));
            Parent root = loader.load();
            ComptesController controller = loader.getController();
            controller.setAccountService(accountService);
            controller.setCurrentCustomer(currentCustomer);
            Stage stage = (Stage) btnComptes.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mes Comptes");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page des comptes.");
        }
    }

    @FXML
    private void handleTransfert(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Transfert.fxml"));
            Parent root = loader.load();
            TransfertController controller = loader.getController();
            controller.setAccountService(accountService);
            controller.setCurrentCustomer(currentCustomer);
            Stage stage = (Stage) btnTransfert.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transfert");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de transfert.");
        }
    }

    @FXML
    private void handleProfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Profil.fxml"));
            Parent root = loader.load();
            ProfilController controller = loader.getController();
            controller.setCustomerService(customerService);
            controller.setCurrentCustomer(currentCustomer);
            Stage stage = (Stage) btnProfil.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de profil.");
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

    @FXML
    private void handleRafraichir(ActionEvent event) {
        loadTransactions();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCustomerService(CustomerService customerService) {
    }

    public void setCurrentCustomer(Custumer customer) {
    }
}