package syntax;

import lexicon.Token;

import java.util.ArrayList;

// MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
public class MulExp implements Unit{
    private static final UnitType type = UnitType.MUL_EXP;
    private final ArrayList<UnaryExp> unaryExps = new ArrayList<>();
    private final ArrayList<Token> operators = new ArrayList<>();

    public MulExp(ArrayList<UnaryExp> unaryExps, ArrayList<Token> operators) {
        this.unaryExps.addAll(unaryExps);
        this.operators.addAll(operators);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < unaryExps.size(); i++) {
            sb.append(unaryExps.get(i));
            if (i < operators.size()) {
                sb.append(" ").append(operators.get(i)).append(" ");
            }
        }
        return sb.toString();
    }
}
