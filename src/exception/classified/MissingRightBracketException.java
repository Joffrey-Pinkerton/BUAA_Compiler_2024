package exception.classified;

import core.Handler;
import exception.ErrorCode;
import exception.ErrorInfo;

public class MissingRightBracketException extends RuntimeException {
    public MissingRightBracketException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.K, lineNum));
    }
}
