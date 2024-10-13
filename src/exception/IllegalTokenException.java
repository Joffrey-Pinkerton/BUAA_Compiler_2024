package exception;

import top.Handler;

public class IllegalTokenException extends RuntimeException {
    public IllegalTokenException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.A, lineNum));
    }
}
