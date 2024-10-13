package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import core.Handler;

public class ArgumentCountMismatchException extends RuntimeException {
    public ArgumentCountMismatchException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.D, lineNum));
    }
}
