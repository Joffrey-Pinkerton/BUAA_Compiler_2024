package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import top.Handler;

public class BreakContinueOutsideLoopException extends RuntimeException {
    public BreakContinueOutsideLoopException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.M, lineNum));
    }
}
