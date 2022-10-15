package ru.otus.orm.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cashe.HwCache;
import ru.otus.orm.otus.core.repository.DataTemplate;
import ru.otus.orm.otus.core.sessionmanager.TransactionRunner;
import ru.otus.orm.otus.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private HwCache<Long, Manager> cache;

    public void setCashe(HwCache<Long, Manager> cache) {
        this.cache = cache;
    }

    public DbServiceManagerImpl(TransactionRunner transactionRunner,
                                DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
    }

    @Override
    public Manager saveManager(Manager manager) {
        Manager savedManager = transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
        if (this.cache != null) {
            this.cache.put(savedManager.getNo(), savedManager);
        }
        return savedManager;
    }

    @Override
    public Optional<Manager> getManager(long no) {
        if (this.cache != null) {
            Optional<Manager> managerOpt = Optional.ofNullable(this.cache.get(no));
            if (managerOpt.isEmpty()) {
                Optional<Manager> managerOpt2 = getManagerFromDb(no);
                managerOpt2.ifPresent(manager -> this.cache.put(manager.getNo(), manager));
                return managerOpt2;
            }
            return managerOpt;
        }
        return getManagerFromDb(no);
    }

    private Optional<Manager> getManagerFromDb(long no) {
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }
}
