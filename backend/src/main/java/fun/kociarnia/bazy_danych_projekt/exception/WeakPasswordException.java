package fun.kociarnia.bazy_danych_projekt.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class WeakPasswordException extends RuntimeException {
    private final List<String> errors;

    public WeakPasswordException(List<String> errors) {
        super("Password is too weak");
        this.errors = errors;
    }

}
