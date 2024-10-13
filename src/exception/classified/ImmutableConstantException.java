package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import top.Handler;

public class ImmutableConstantException extends RuntimeException {
    public ImmutableConstantException(String message,int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.H, lineNum));
    }
}
