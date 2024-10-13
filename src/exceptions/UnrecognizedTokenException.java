package exceptions;

import output.Handler;

public class UnrecognizedTokenException extends RuntimeException {
    public UnrecognizedTokenException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.A, lineNum));
    }
}
