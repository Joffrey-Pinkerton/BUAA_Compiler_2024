package exception.classified;

import exception.ErrorCode;
import exception.ErrorInfo;
import core.Handler;

public class PrintFormatMismatchException extends RuntimeException {
    public PrintFormatMismatchException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.L, lineNum));
    }
}
