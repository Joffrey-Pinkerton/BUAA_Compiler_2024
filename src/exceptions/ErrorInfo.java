package exceptions;

public class ErrorInfo {
    private final int lineNum;
    private final ErrorCode id;

    public ErrorInfo(ErrorCode id, int lineNum) {
        this.lineNum = lineNum;
        this.id = id;
    }

    public int getLine() {
        return lineNum;
    }

    public ErrorCode getId() {
        return id;
    }

    @Override
    public String toString() {
        return lineNum + " " + id;
    }
}
