package syntax;

import lexicon.Token;

import java.util.ArrayList;

// RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
public class RelExp implements Unit{
    private final ArrayList<AddExp> addExps = new ArrayList<>();
    private final ArrayList<Token> relops = new ArrayList<>();

    public RelExp(ArrayList<AddExp> addExps, ArrayList<Token> relops) {
        this.addExps.addAll(addExps);
        this.relops.addAll(relops);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addExps.size(); i++) {
            sb.append(addExps.get(i).toString());
            if (i < relops.size()) {
                sb.append(" ").append(relops.get(i)).append(" ");
            }
        }
        return sb.toString();
    }
}
