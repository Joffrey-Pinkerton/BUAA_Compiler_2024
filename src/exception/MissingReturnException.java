package exception;

import core.Handler;

public class MissingReturnException extends RuntimeException {
    public MissingReturnException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.G, lineNum));
    }
}
