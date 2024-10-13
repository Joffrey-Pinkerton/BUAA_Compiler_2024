package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import core.Handler;

public class ArgumentTypeMismatchException extends RuntimeException {
    public ArgumentTypeMismatchException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.E, lineNum));
    }
}
