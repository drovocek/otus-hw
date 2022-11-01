package otus.jdbc.mapper.core.exceptions;

public class RowMappingException extends RuntimeException {

    public RowMappingException(Throwable throwable) {
        super(throwable);
    }

    public RowMappingException(String message) {
        super(message);
    }
}
