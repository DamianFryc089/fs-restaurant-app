package fun.kociarnia.bazy_danych_projekt.exception;

public class SamePasswordException extends RuntimeException {
    public SamePasswordException() {
        super("New password cannot be the same as the old password");
    }
}
