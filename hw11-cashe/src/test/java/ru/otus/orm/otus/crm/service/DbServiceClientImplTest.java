package ru.otus.orm.otus.crm.service;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cashe.MyCache;
import ru.otus.orm.mapper.DataTemplateJdbc;
import ru.otus.orm.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.orm.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.orm.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.orm.otus.crm.model.Client;

import javax.sql.DataSource;

class DbServiceClientImplTest {

    private static final String URL = "jdbc:postgresql://localhost:5430/hw11DB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImplTest.class);

    private DbServiceClientImpl serviceClient;

    @BeforeEach
    void prepareService() {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, Client.class);
        this.serviceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
    }

    @Test
    @DisplayName("с включенным кешем должен работать быстрее")
    void fasterWithCache() {
        Assertions.assertNull(this.serviceClient.getCache());
        for (int i = 0; i < 10; i++) {
            this.serviceClient.saveClient(new Client("№" + i));
        }

        //timer without cache
        long startTimeWithoutCache = System.nanoTime();
        for (int i = 1; i < 11; i++) {
            this.serviceClient.getClient(i);
        }
        long endTimeWithoutCache = System.nanoTime();
        long timeWithoutCache = endTimeWithoutCache - startTimeWithoutCache;

        this.serviceClient.setCache(new MyCache<>());
        Assertions.assertNotNull(this.serviceClient.getCache());

        //fill cache
        for (int i = 1; i < 11; i++) {
            this.serviceClient.getClient(i);
        }

        //timer with cache
        long startTimeWithCache = System.nanoTime();
        for (int i = 1; i < 11; i++) {
            this.serviceClient.getClient(i);
        }
        long endTimeWithCache = System.nanoTime();
        long timeWithCache = endTimeWithCache - startTimeWithCache;

        System.out.println(timeWithCache);
        System.out.println(timeWithoutCache);
        Assertions.assertTrue(timeWithCache < timeWithoutCache);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}