package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import top.Handler;

public class MissingReturnException extends RuntimeException {
    public MissingReturnException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.G, lineNum));
    }
}
