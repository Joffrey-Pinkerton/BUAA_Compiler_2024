package syntax;

// ConstExp → AddExp [note: Ident used must be constant]
public class ConstExp implements Unit{
    public final AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public String toString() {
        return addExp.toString();
    }
}
