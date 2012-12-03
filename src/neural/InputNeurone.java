package neural;

import equation.Equation;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 22:02
 */
public class InputNeurone extends NeuroneImpl {
    double value;

    public InputNeurone() {
        super(null);
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void fire() {
        // Don't do anything.
    }

}
