package syntax;

// Exp → AddExp
public class Exp implements Unit{
    public final AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public String toString() {
        return addExp.toString();
    }
}
