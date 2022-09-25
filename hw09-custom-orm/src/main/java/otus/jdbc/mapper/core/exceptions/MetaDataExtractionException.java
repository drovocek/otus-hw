package otus.jdbc.mapper.core.exceptions;

public class MetaDataExtractionException extends RuntimeException {

    public final static String NOT_CONSTRUCTOR_WITH_NO_ARGUMENTS = "Please add a constructor with no arguments to the class %s";
    public final static String NO_ID_ANNOTATION = "Please add @Id annotation above the id field to %s";

    public MetaDataExtractionException(String message) {
        super(message);
    }
}
