package otus.otus.core.sessionmanager;

public interface TransactionRunner {

    <T> T doInTransaction(TransactionAction<T> action);
}
