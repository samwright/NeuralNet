package equation;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 04/12/2012
 * Time: 21:43
 */
public class Identity implements Equation {
    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public double evaluateDiff(double x) {
        return 0;
    }
}
