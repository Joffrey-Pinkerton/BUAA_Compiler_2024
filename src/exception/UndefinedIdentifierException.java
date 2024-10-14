package exception;

import core.Handler;

public class UndefinedIdentifierException extends RuntimeException {
    public UndefinedIdentifierException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.C, lineNum));
    }
}
