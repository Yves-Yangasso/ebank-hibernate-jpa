package yang.bao.yang_bank.service.impl;

import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.repository.CustomerRepository;
import yang.bao.yang_bank.service.CustomerService;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;

    public CustomerServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.customerRepository = new CustomerRepository(entityManager);
    }

    @Override
    public Custumer createCustomer(String firstname, String lastname, String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        Custumer customer = new Custumer();
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setUsername(username);
        customer.setPassword(password);
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public Custumer getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public Custumer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public List<Custumer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void updateCustomer(Custumer customer) {
        customerRepository.update(customer);
    }

    @Override
    public void deleteCustomer(Custumer customer) {
        customerRepository.delete(customer);
    }
}