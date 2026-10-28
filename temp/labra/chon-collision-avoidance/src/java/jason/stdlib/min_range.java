package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class min_range extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length != 4 || !args[0].isList() || !args[1].isNumeric() || !args[2].isNumeric()) {
            return false;
        }

        ListTerm ranges = (ListTerm) args[0];
        int start = (int) ((NumberTerm) args[1]).solve();
        int end = (int) ((NumberTerm) args[2]).solve();

        if (ranges.isEmpty()) {
            return false;
        }

        int from = Math.max(0, Math.min(start, end));
        int to = Math.min(ranges.size() - 1, Math.max(start, end));
        double min = Double.POSITIVE_INFINITY;

        for (int i = from; i <= to; i++) {
            Term value = ranges.get(i);
            if (value.isNumeric()) {
                double distance = ((NumberTerm) value).solve();
                if (Double.isFinite(distance) && distance < min) {
                    min = distance;
                }
            }
        }

        if (!Double.isFinite(min)) {
            return false;
        }

        return un.unifies(args[3], ASSyntax.createNumber(min));
    }
}
