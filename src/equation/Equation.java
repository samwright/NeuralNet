package equation;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 19:44
 */
public interface Equation {

    /**
     * Evaluate the equation for the given value of x.
     *
     * @param x The input variable.
     * @return The evaluation of this equation for x.
     */
    public double evaluate(double x);

    /**
     * Evaluate the differential of the equation for the given value of x.
     *
     * @param x The input variable.
     * @return The evaluation of the differential of this equation for x.
     */
    public double evaluateDiff(double x);
}
