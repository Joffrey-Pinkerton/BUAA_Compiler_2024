package exception.unclassified;

public class UnexpectedErrorException extends RuntimeException {
  public UnexpectedErrorException(String message) {
    super(message);
  }
}
