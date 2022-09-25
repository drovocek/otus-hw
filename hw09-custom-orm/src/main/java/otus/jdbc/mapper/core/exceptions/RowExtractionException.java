package otus.jdbc.mapper.core.exceptions;

public class RowExtractionException extends RuntimeException {

    public RowExtractionException(Throwable throwable) {
        super(throwable);
    }

    public RowExtractionException(String message) {
        super(message);
    }
}
