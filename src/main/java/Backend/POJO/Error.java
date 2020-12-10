package Backend.POJO;

public class Error {
    private final ErrorType type;

    public Error(String type) {
        this.type = ErrorType.valueOf(type);
    }

    public ErrorType getType() {
        return type;
    }

    public enum ErrorType {
        ERROR,
        WARN,
        DEBUG
    }
}
