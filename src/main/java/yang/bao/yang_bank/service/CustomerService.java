package yang.bao.yang_bank.service;

import yang.bao.yang_bank.entity.Custumer;

import java.util.List;

public interface CustomerService {
    Custumer createCustomer(String firstname, String lastname, String username, String password);
    Custumer getCustomerById(Integer id);
    Custumer getCustomerByUsername(String username);
    List<Custumer> getAllCustomers();
    void updateCustomer(Custumer customer);
    void deleteCustomer(Custumer customer);
    boolean loginCustomer(String username, String password);
}