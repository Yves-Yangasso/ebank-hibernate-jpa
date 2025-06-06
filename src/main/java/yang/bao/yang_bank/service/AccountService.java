package yang.bao.yang_bank.service;

import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Custumer;

import java.util.List;

public interface AccountService {
    Account createAccount(Custumer custumer, Double initialBalance);
    Account getAccountById(Integer id);
    List<Account> getAllAccounts();
    List<Account> getAccountsByCustomer(Custumer custumer);
    void updateAccount(Account account);
    void deleteAccount(Account account);
    void deposit(Account account, Double amount) throws Exception;
    void withdraw(Account account, Double amount) throws Exception;
    void transfer(Account fromAccount, Account toAccount, Double amount) throws Exception;
}