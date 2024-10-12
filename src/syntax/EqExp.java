package syntax;

import lexicon.Token;

import java.util.ArrayList;

//  EqExp → RelExp | EqExp ('==' | '!=') RelExp
public class EqExp implements Unit{
    private final ArrayList<RelExp> relExps = new ArrayList<>();
    private final ArrayList<Token> operators = new ArrayList<>();

    public EqExp(ArrayList<RelExp> relExps, ArrayList<Token> operators) {
        this.relExps.addAll(relExps);
        this.operators.addAll(operators);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < relExps.size(); i++) {
            sb.append(relExps.get(i).toString());
            if (i < operators.size()) {
                sb.append(" ").append(operators.get(i).toString()).append(" ");
            }
        }
        return sb.toString();
    }
}
