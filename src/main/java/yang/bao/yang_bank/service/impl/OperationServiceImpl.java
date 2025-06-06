package yang.bao.yang_bank.service.impl;

import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Operation;
import yang.bao.yang_bank.repository.OperationRepository;
import yang.bao.yang_bank.service.OperationService;

import javax.persistence.EntityManager;
import java.util.List;

public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final EntityManager entityManager;

    public OperationServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.operationRepository = new OperationRepository(entityManager);
    }

    @Override
    public Operation createOperation(Account account, Double amount, String typeOperation) {
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAmount(amount);
        operation.setOperationDate(java.time.Instant.now());
        operation.setTypeOperation(typeOperation);
        operationRepository.save(operation);
        return operation;
    }

    @Override
    public Operation getOperationById(Integer id) {
        return operationRepository.findById(id);
    }

    @Override
    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    @Override
    public List<Operation> getOperationsByAccount(Account account) {
        return operationRepository.findByAccount(account);
    }

    @Override
    public void updateOperation(Operation operation) {
        operationRepository.update(operation);
    }

    @Override
    public void deleteOperation(Operation operation) {
        operationRepository.delete(operation);
    }
}