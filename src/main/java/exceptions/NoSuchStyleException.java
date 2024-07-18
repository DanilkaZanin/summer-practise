package exceptions;

public class NoSuchStyleException extends RuntimeException {
    public NoSuchStyleException(String message) {
        super("Стиля с названием " + message + " нет в списке!");
    }
}
