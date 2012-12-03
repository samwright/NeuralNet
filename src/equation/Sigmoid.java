package equation;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 19:53
 */
public class Sigmoid implements Equation {
    private static final double temperature_param = 2;
    @Override
    public double evaluate(double x) {
        return 1/(1+Math.exp(-x/temperature_param)) * 2 - 1;
    }

    @Override
    public double evaluateDiff(double x) {
        double learning_rate = 0.1;
        double momentum = 0.1;

        double f_of_x = (evaluate(x) + 1) / 2;

        return 2 * f_of_x * (1 - f_of_x) / temperature_param;
    }
}
