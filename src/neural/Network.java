package neural;

import equation.Equation;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 20:25
 */
public interface Network {

    /**
     * Gets the first layer (the input layer).
     *
     * @return Input layer.
     */
    Layer getInputLayer();

    /**
     * Gets the last layer (the output layer).
     *
     * @return Output layer.
     */
    Layer getOutputLayer();

    /**
     * Propagate input through network then return the output neurone values.
     *
     * @param input Input neurone values.
     * @return Output values.
     */
    double[] evaluate(double[] input);

    /**
     * Create a layer object.
     *
     * @return A layer object.
     */
    Layer createLayer();

    /**
     * Returns the transition equation used in the network.
     *
     * @return The transition equation.
     */
    Equation getTransitionEquation();

    /**
     * Gets the number of layers (including the input and output layers) in the net.
     *
     * @return The number of layers (including the input and output layers) in the net.
     */
    int getNumberOfLayers();
}
