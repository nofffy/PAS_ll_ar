package Exeptions;

public class UserLoginAlreadyUsedException extends RuntimeException {
    public UserLoginAlreadyUsedException(String message) {
        super(message);
    }
}
