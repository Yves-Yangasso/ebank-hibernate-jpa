package yang.bao.yang_bank.service.impl;

import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Custumer;
import yang.bao.yang_bank.entity.Operation;
import yang.bao.yang_bank.repository.AccountRepository;
import yang.bao.yang_bank.repository.OperationRepository;
import yang.bao.yang_bank.service.AccountService;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final EntityManager entityManager;

    public AccountServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.accountRepository = new AccountRepository(entityManager);
        this.operationRepository = new OperationRepository(entityManager);
    }

    @Override
    public Account createAccount(Custumer custumer, Double initialBalance) {
        if (custumer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        Account account = new Account();
        account.setCustumer(custumer);
        account.setBalance(initialBalance);
        account.setCreationDate(Instant.now());
        accountRepository.save(account);
        return account;
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAccountsByCustomer(Custumer custumer) {
        return accountRepository.findByCustomer(custumer);
    }

    @Override
    public void updateAccount(Account account) {
        accountRepository.update(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    @Override
    public void deposit(Account account, Double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        entityManager.getTransaction().begin();
        try {
            account.setBalance(account.getBalance() + amount);
            Operation operation = new Operation();
            operation.setAccount(account);
            operation.setAmount(amount);
            operation.setOperationDate(Instant.now());
            operation.setTypeOperation("DEPOSIT");
            operationRepository.save(operation);
            accountRepository.update(account);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new Exception("Deposit failed: " + e.getMessage());
        }
    }

    @Override
    public void withdraw(Account account, Double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (account.getBalance() < amount) {
            throw new Exception("Insufficient funds");
        }
        entityManager.getTransaction().begin();
        try {
            account.setBalance(account.getBalance() - amount);
            Operation operation = new Operation();
            operation.setAccount(account);
            operation.setAmount(-amount);
            operation.setOperationDate(Instant.now());
            operation.setTypeOperation("WITHDRAWAL");
            operationRepository.save(operation);
            accountRepository.update(account);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new Exception("Withdrawal failed: " + e.getMessage());
        }
    }

    @Override
    public void transfer(Account fromAccount, Account toAccount, Double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (fromAccount.getBalance() < amount) {
            throw new Exception("Insufficient funds in source account");
        }
        entityManager.getTransaction().begin();
        try {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            Operation withdrawal = new Operation();
            withdrawal.setAccount(fromAccount);
            withdrawal.setAmount(-amount);
            withdrawal.setOperationDate(Instant.now());
            withdrawal.setTypeOperation("TRANSFER_OUT");
            operationRepository.save(withdrawal);

            Operation deposit = new Operation();
            deposit.setAccount(toAccount);
            deposit.setAmount(amount);
            deposit.setOperationDate(Instant.now());
            deposit.setTypeOperation("TRANSFER_IN");
            operationRepository.save(deposit);

            accountRepository.update(fromAccount);
            accountRepository.update(toAccount);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new Exception("Transfer failed: " + e.getMessage());
        }
    }
}