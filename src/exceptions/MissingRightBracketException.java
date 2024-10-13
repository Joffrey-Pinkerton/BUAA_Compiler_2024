package exceptions;

import output.Handler;

public class MissingRightBracketException extends RuntimeException {
    public MissingRightBracketException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.K, lineNum));
    }
}
