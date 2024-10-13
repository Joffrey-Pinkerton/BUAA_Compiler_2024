package exceptions;

import output.Handler;

public class MissingSemicolonException extends RuntimeException {
    public MissingSemicolonException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.I, lineNum));
    }
}
