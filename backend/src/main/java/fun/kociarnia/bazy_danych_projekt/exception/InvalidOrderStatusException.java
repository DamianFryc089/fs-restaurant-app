package fun.kociarnia.bazy_danych_projekt.exception;


public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}
