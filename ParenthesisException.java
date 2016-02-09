public class ParenthesisException extends Exception {
    public ParenthesisException() {
        super("Invalid file due to inclusion of parenthesis.");
    }

    public ParenthesisException(String msg) {
        super(msg);
    }
}