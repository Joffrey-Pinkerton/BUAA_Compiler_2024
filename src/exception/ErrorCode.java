package exception;

public enum ErrorCode {
    A, B, C, D, E, F, G, H, I, J, K, L, M;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
