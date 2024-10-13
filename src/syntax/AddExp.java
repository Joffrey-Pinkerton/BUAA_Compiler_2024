package syntax;

import lexicon.Token;

import java.util.ArrayList;

// AddExp → MulExp | AddExp ('+' | '−') MulExp
public class AddExp implements Unit {
    private static final UnitType type = UnitType.ADD_EXP;
    private final ArrayList<MulExp> mulExps = new ArrayList<>();
    private final ArrayList<Token> operators = new ArrayList<>();

    public AddExp(ArrayList<MulExp> mulExps, ArrayList<Token> operators) {
        this.mulExps.addAll(mulExps);
        this.operators.addAll(operators);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mulExps.size(); i++) {
            sb.append(mulExps.get(i));
            if (i < operators.size()) {
                sb.append(" ").append(operators.get(i)).append(" ");
            }
        }
        return sb.toString();
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
