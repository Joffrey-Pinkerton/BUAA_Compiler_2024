package exception.classified;

import core.Handler;
import exception.ErrorCode;
import exception.ErrorInfo;

public class MissingSemicolonException extends RuntimeException {
    public MissingSemicolonException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.I, lineNum));
    }
}
