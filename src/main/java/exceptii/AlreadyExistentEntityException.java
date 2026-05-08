package exceptii;

public class AlreadyExistentEntityException extends RuntimeException {
    public AlreadyExistentEntityException(String message) {
        super(message);
    }
}
