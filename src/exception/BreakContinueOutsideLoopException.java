package exception;

import core.Handler;

public class BreakContinueOutsideLoopException extends RuntimeException {
    public BreakContinueOutsideLoopException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.M, lineNum));
    }
}
