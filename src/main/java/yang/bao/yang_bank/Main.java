package yang.bao.yang_bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.io.IOException;
import java.util.List;

public class Main extends Application {
    private EntityManagerFactory emf;
    private EntityManager em;
    private CustomerService customerService;
    private AccountService accountService;
    private OperationService operationService;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialisation JPA
        emf = Persistence.createEntityManagerFactory("yang_bank_pu");
        em = emf.createEntityManager();

        // Initialisation des services
        customerService = new CustomerServiceImpl(em);
        accountService = new AccountServiceImpl(em);
        operationService = new OperationServiceImpl(em);

        // Chargement de l'interface de connexion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));

        Parent root = loader.load();

        // (Optionnel) Passage des services au contrôleur
        // LoginController controller = loader.getController();
        // controller.setServices(customerService, accountService, operationService);

        Scene scene = new Scene(root);
        stage.setTitle("Page de connexion");
        stage.setScene(scene);
        stage.show();

        // (Optionnel) Lancer un test en console
        // runConsoleTest();
    }

    @Override
    public void stop() {
        // Libération des ressources JPA
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    private void runConsoleTest() {
        try {
            // Création de clients
            Custumer john = customerService.createCustomer("John", "Doe", "john_doe", "password123");
            Custumer alice = customerService.createCustomer("Alice", "Smith", "alice_smith", "password456");

            // Création de comptes
            Account johnAccount = accountService.createAccount(john, 1000.0);
            Account aliceAccount = accountService.createAccount(alice, 500.0);
            Account johnSavings = accountService.createAccount(john, 2000.0);

            // Opérations bancaires
            accountService.deposit(johnAccount, 500.0);
            accountService.withdraw(aliceAccount, 200.0);
            accountService.transfer(johnAccount, aliceAccount, 300.0);

            // Affichage des clients
            System.out.println("--- Tous les clients ---");
            List<Custumer> allCustomers = customerService.getAllCustomers();
            allCustomers.forEach(customer ->
                    System.out.println("ID: " + customer.getId() +
                            ", Nom: " + customer.getFirstname() + " " + customer.getLastname() +
                            ", Username: " + customer.getUsername())
            );

            // Affichage des comptes
            System.out.println("\n--- Tous les comptes ---");
            List<Account> allAccounts = accountService.getAllAccounts();
            allAccounts.forEach(account ->
                    System.out.println("ID: " + account.getId() +
                            ", Client: " + account.getCustumer().getFirstname() +
                            ", Solde: " + account.getBalance() +
                            ", Créé le: " + account.getCreationDate())
            );

            // Comptes de John
            System.out.println("\n--- Comptes de John ---");
            List<Account> johnAccounts = accountService.getAccountsByCustomer(john);
            johnAccounts.forEach(account ->
                    System.out.println("ID: " + account.getId() +
                            ", Solde: " + account.getBalance() +
                            ", Créé le: " + account.getCreationDate())
            );

            // Opérations de John
            System.out.println("\n--- Opérations du compte principal de John ---");
            List<Operation> johnOperations = operationService.getOperationsByAccount(johnAccount);
            johnOperations.forEach(operation ->
                    System.out.println("ID: " + operation.getId() +
                            ", Type: " + operation.getTypeOperation() +
                            ", Montant: " + operation.getAmount() +
                            ", Date: " + operation.getOperationDate())
            );

            // Toutes les opérations
            System.out.println("\n--- Toutes les opérations ---");
            List<Operation> allOperations = operationService.getAllOperations();
            allOperations.forEach(operation ->
                    System.out.println("ID: " + operation.getId() +
                            ", Compte ID: " + operation.getAccount().getId() +
                            ", Type: " + operation.getTypeOperation() +
                            ", Montant: " + operation.getAmount() +
                            ", Date: " + operation.getOperationDate())
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
