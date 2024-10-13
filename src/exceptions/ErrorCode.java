package exceptions;

public enum ErrorCode {
    A, B, C, D, E, F, G, H, I, J, K;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
