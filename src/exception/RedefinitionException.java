package exception;

import core.Handler;

public class RedefinitionException extends RuntimeException {
    public RedefinitionException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.B, lineNum));
    }
}
