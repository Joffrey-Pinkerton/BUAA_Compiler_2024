package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import top.Handler;

public class IllegalIdentifierException extends RuntimeException {
    public IllegalIdentifierException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.A, lineNum));
    }
}
