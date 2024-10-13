package exceptions;

import output.Handler;

public class MissingRightParenthesisException extends RuntimeException {
    public MissingRightParenthesisException(String message, int lineNum) {
        super(message);
        Handler.addErrorInfo(new ErrorInfo(ErrorCode.J, lineNum));
    }
}
