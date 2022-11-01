package ru.otus.orm.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cashe.HwCache;
import ru.otus.orm.otus.core.repository.DataTemplate;
import ru.otus.orm.otus.core.sessionmanager.TransactionRunner;
import ru.otus.orm.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private HwCache<Long, Client> cache;

    public void setCache(HwCache<Long, Client> cache) {
        this.cache = cache;
    }

    public HwCache<Long, Client> getCache() {
        return this.cache;
    }

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = this.transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = this.dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            this.dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
        if (this.cache != null) {
            this.cache.put(savedClient.getId(), savedClient);
        }
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        if (this.cache != null) {
            Optional<Client> clientOpt = Optional.ofNullable(this.cache.get(id));
            if (clientOpt.isEmpty()) {
                Optional<Client> clientOpt2 = getClientFromDb(id);
                clientOpt2.ifPresent(client -> this.cache.put(client.getId(), client));
                return clientOpt2;
            }
            return clientOpt;
        }
        return getClientFromDb(id);
    }

    private Optional<Client> getClientFromDb(long id) {
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return this.transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            if (this.cache != null) {
                clientList.forEach(client -> this.cache.put(client.getId(), client));
            }
            return clientList;
        });
    }
}
