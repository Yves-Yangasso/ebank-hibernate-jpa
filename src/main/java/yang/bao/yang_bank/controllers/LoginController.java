package yang.bao.yang_bank.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
    private void handleConnexion() {
        String username = txt_username.getText();
        String password = txt_mdp.getText();
        // TODO: Implement login logic
        // Example: Verify credentials using customerService
        System.out.println("Login attempted: Username=" + username + ", Password=" + password);
        // Add logic to load next scene (e.g., dashboard) on success
    }

    @FXML
    private void handleInscription() {
        String username = txt_username.getText();
        String password = txt_mdp.getText();
        // TODO: Implement registration logic
        // Example: Create new customer
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password are required");
            return;
        }
        // Split username into first/last name (simplified)
        String[] names = username.split("\\s+");
        String firstName = names.length > 0 ? names[0] : "";
        String lastName = names.length > 1 ? names[1] : "";
        try {
            customerService.createCustomer(firstName, lastName, username, password);
            System.out.println("Registered: " + username);
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
}