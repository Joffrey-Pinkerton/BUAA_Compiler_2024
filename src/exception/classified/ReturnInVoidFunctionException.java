package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import core.Handler;

public class ReturnInVoidFunctionException extends RuntimeException {
    public ReturnInVoidFunctionException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.F, lineNum));
    }
}
