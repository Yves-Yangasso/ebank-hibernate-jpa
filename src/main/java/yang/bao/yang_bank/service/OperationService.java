package yang.bao.yang_bank.service;

import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Operation;

import java.util.List;

public interface OperationService {
    Operation createOperation(Account account, Double amount, String typeOperation);
    Operation getOperationById(Integer id);
    List<Operation> getAllOperations();
    List<Operation> getOperationsByAccount(Account account);
    void updateOperation(Operation operation);
    void deleteOperation(Operation operation);
}