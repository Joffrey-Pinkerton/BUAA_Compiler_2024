package exception.classified;

import core.Handler;
import exception.ErrorInfo;
import exception.ErrorCode;

public class MissingRightParenthesisException extends RuntimeException {
    public MissingRightParenthesisException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.J, lineNum));
    }
}
