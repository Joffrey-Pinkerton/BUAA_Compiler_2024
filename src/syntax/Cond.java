package syntax;

//  Cond → LOrExp
public class Cond implements Unit{
    private final LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public String toString() {
        return lOrExp.toString();
    }

}
